# THDB Runner Evidence Pack (_07 + Gz lane)

This document defines the exact artifacts required once the THDB self-hosted runner is online.

## Single-command evidence collection

```bash
./scripts/thdb_collect_evidence.sh
```

Generated outputs (all under `artifacts/thdb/`):

- `00-run-meta.log` — runner/runtime metadata and Gradle version output
- `01-compile.log` — `assembleDebug` compile/build output
- `02-unit-tests.log` — full debug unit test output
- `03-kill-switch-tests.log` — focused kill-switch validation output
- `04-ad-id-proof.log` — explicit PASS/FAIL proof for merged manifest AD_ID presence
- `merged-manifest-debug.xml` — copied merged debug manifest used for proof
- `05-sha256sums.txt` — hashes for integrity pinning

## Required publication payload (pinned SHA URLs)

After runner execution, commit evidence files into repo and publish immutable links:

1. **Compile/runtime logs**
   - `artifacts/thdb/01-compile.log`
   - `artifacts/thdb/03-kill-switch-tests.log`
2. **Merged-manifest AD_ID proof**
   - `artifacts/thdb/04-ad-id-proof.log`
   - `artifacts/thdb/merged-manifest-debug.xml`
3. **Integrity manifest**
   - `artifacts/thdb/05-sha256sums.txt`

Pinned URL template:

```text
https://github.com/mgnlia/kidrunner-android-prototype/blob/<commit-sha>/artifacts/thdb/<file>
```

## CI path

GitHub Actions workflow: `.github/workflows/thdb-android-evidence.yml`

- Trigger: push to `main` (matching Android/evidence paths) or manual dispatch
- Runner: self-hosted (THDB)
- Uploads the same evidence pack as artifact `thdb-evidence-<sha>`
