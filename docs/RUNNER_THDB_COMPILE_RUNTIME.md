# THDB Runner Packet — Compile + Runtime Verification Steps

Task: `THDBokxNJUA91tOrSemxq`

Purpose: run deterministic compile/test/runtime checks for `_07` + `Gz` lane with **fail-closed defaults** intact.

## 0) Preconditions (must be true)

- OS: Ubuntu 22.04+ (or equivalent Linux CI image)
- JDK: 17
- Android SDK installed with command-line tools
- Emulator acceleration available (KVM) for runtime lane
- Repo checked out at latest `main`

## 1) Install toolchain (if runner is fresh)

```bash
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk unzip wget

export ANDROID_SDK_ROOT="$HOME/android-sdk"
mkdir -p "$ANDROID_SDK_ROOT/cmdline-tools"

cd /tmp
wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O cmdline-tools.zip
unzip -q cmdline-tools.zip -d cmdline-tools
mv cmdline-tools/cmdline-tools "$ANDROID_SDK_ROOT/cmdline-tools/latest"

export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools:$ANDROID_SDK_ROOT/emulator:$PATH"

yes | sdkmanager --licenses >/dev/null
sdkmanager \
  "platform-tools" \
  "platforms;android-34" \
  "build-tools;34.0.0" \
  "emulator" \
  "system-images;android-34;google_apis;x86_64"
```

## 2) Compile + unit test commands (exact)

From repository root:

```bash
./gradlew --version
./gradlew clean :app:assembleDebug :app:testDebugUnitTest --stacktrace
```

Expected outcome:
- `BUILD SUCCESSFUL`
- Unit tests include `AdEligibilityGuardTest` and pass unknown-age fail-closed assertions.

## 3) Static verification of fail-closed defaults

Verify these `BuildConfig` defaults are present in `app/build.gradle.kts`:

- `KIDSAFE_ADS_ENABLED=true`
- `POLICY_DECLARATIONS_FINALIZED=false`
- `ADMOB_PROD_IDS_APPROVED=false`
- `ADMOB_BANNER_AD_UNIT_ID=""`

These keep ad path blocked until policy decision task `VhVH7XGVqp5oNRb42Wx6m` finalizes.

## 4) Runtime verification (emulator)

### 4.1 Create and boot AVD

```bash
echo "no" | avdmanager create avd -n kidsafeApi34 -k "system-images;android-34;google_apis;x86_64" -d pixel_5
emulator -avd kidsafeApi34 -no-window -no-audio -no-boot-anim &
adb wait-for-device
adb shell getprop sys.boot_completed | tr -d '\r'
```

Proceed once output is `1`.

### 4.2 Install and launch app

```bash
./gradlew :app:installDebug
adb shell am start -n com.gz.kidsafe/.MainActivity
sleep 3
adb logcat -d | grep -E "KidSafeAdsManager|KidSafeBannerAdView"
```

Expected runtime evidence (fail-closed):

- `KidSafeAdsManager: Ads initialization blocked (reason=AGE_UNKNOWN)`
- `KidSafeBannerAdView: Banner blocked by fail-closed guard (reason=AGE_UNKNOWN)`

Any `MobileAds initialized in kid-safe mode` log before unknown-age policy decision is a blocker and should fail THDB verification.

## 5) Artifact capture (required for handoff)

Collect and upload:

1. `build_scan.txt` (stdout from gradle compile/test)
2. `unit_test_report.zip` from `app/build/reports/tests/testDebugUnitTest`
3. `runtime_logcat.txt` containing the fail-closed log lines above
4. `merged_manifest_debug.xml` from manifest-merger output (if enabled by runner lane)

Suggested commands:

```bash
./gradlew :app:processDebugMainManifest
find app/build -name "AndroidManifest.xml" | head -n 5
adb logcat -d > runtime_logcat.txt
```

## 6) Pass/Fail criteria for THDB

Pass only if all are true:

- Debug APK compiles.
- Unit tests pass.
- Unknown-age path blocks app init and banner load at runtime.
- No evidence of ad serving path activation under defaults.

Fail if any ad init/load occurs with `AgeSignal.UNKNOWN` or while `POLICY_DECLARATIONS_FINALIZED=false`.
