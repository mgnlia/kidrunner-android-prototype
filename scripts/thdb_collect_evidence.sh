#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT_DIR="$ROOT_DIR/artifacts/thdb"
mkdir -p "$OUT_DIR"

timestamp() {
  date -u +"%Y-%m-%dT%H:%M:%SZ"
}

run_and_capture() {
  local log_file="$1"
  shift

  echo "[$(timestamp)] $*" | tee -a "$log_file"
  "$@" 2>&1 | tee -a "$log_file"
}

cd "$ROOT_DIR"

echo "[$(timestamp)] THDB evidence run start" | tee "$OUT_DIR/00-run-meta.log"
run_and_capture "$OUT_DIR/00-run-meta.log" ./gradlew --version --no-daemon
run_and_capture "$OUT_DIR/01-compile.log" ./gradlew clean assembleDebug --stacktrace --no-daemon
run_and_capture "$OUT_DIR/02-unit-tests.log" ./gradlew testDebugUnitTest --stacktrace --no-daemon --info
run_and_capture "$OUT_DIR/03-kill-switch-tests.log" ./gradlew testDebugUnitTest --tests "com.gz.kidsafe.ads.KidSafeAdsKillSwitchTest" --stacktrace --no-daemon --info

MANIFEST_PATH="$(find "$ROOT_DIR/app/build/intermediates" -type f -name "AndroidManifest.xml" | grep -E "merged_manifest|merged_manifests" | grep -E "/debug/" | head -n 1 || true)"

if [[ -z "$MANIFEST_PATH" ]]; then
  echo "[$(timestamp)] ERROR: merged debug manifest not found under app/build/intermediates" | tee "$OUT_DIR/04-ad-id-proof.log"
  exit 1
fi

cp "$MANIFEST_PATH" "$OUT_DIR/merged-manifest-debug.xml"

if grep -q "com.google.android.gms.permission.AD_ID" "$OUT_DIR/merged-manifest-debug.xml"; then
  {
    echo "[$(timestamp)] FAIL: AD_ID permission present in merged manifest"
    echo "source_manifest=$MANIFEST_PATH"
    grep -n "com.google.android.gms.permission.AD_ID" "$OUT_DIR/merged-manifest-debug.xml"
  } | tee "$OUT_DIR/04-ad-id-proof.log"
  exit 2
fi

{
  echo "[$(timestamp)] PASS: AD_ID permission absent in merged debug manifest"
  echo "source_manifest=$MANIFEST_PATH"
} | tee "$OUT_DIR/04-ad-id-proof.log"

sha256sum "$OUT_DIR"/*.log "$OUT_DIR/merged-manifest-debug.xml" > "$OUT_DIR/05-sha256sums.txt"

echo "[$(timestamp)] THDB evidence run complete" | tee -a "$OUT_DIR/00-run-meta.log"
