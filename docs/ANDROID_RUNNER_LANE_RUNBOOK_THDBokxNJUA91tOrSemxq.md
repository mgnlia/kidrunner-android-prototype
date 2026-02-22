# Android Compile/Runtime Runner Lane Runbook (Task: `THDBokxNJUA91tOrSemxq`)

## Objective
Provide reproducible **compile + runtime smoke** execution for the Android prototype while preserving kid-safety policy defaults:

- **Fail-closed ads default** (`AgeSignal.UNKNOWN` => `NO-ADS`)
- **MobileAds real wiring gated by feature flags only**

---

## 1) Preconditions

Runner requirements:
- Ubuntu runner with JDK 17
- Android SDK command-line tools installed
- Emulator acceleration available (KVM) for runtime smoke lane

Project assumptions:
- Gradle pinned to **8.7** in CI lane
- AGP currently **8.5.2**
- `KIDSAFE_ADS_ENABLED`, `MOBILE_ADS_REAL_ADAPTER_ENABLED`, and `POLICY_DECLARATIONS_FINALIZED` are controlled by `BuildConfig`/flavor vars

---

## 2) Compile Lane (CI-safe)

### Commands
```bash
gradle --version
gradle assembleDebug --stacktrace --no-daemon
gradle testDebugUnitTest --stacktrace --no-daemon
```

### Required outputs/artifacts
- `app/build/outputs/apk/debug/app-debug.apk`
- Unit test reports:
  - `app/build/test-results/testDebugUnitTest/*.xml`
  - `app/build/reports/tests/testDebugUnitTest/index.html`

### Pass criteria
- `assembleDebug` exits `0`
- `testDebugUnitTest` exits `0`

---

## 3) Runtime Smoke Lane (emulator)

## AVD bootstrap (API 34 example)
```bash
sdkmanager --install "platform-tools" "platforms;android-34" "system-images;android-34;google_apis;x86_64" "emulator"
avdmanager create avd -n kidrunnerApi34 -k "system-images;android-34;google_apis;x86_64" --device "pixel_6"
emulator -avd kidrunnerApi34 -no-window -no-audio -no-snapshot -gpu swiftshader_indirect &
adb wait-for-device
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0
```

### Install + launch
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.gz.kidsafe/.MainActivity
sleep 8
adb shell pidof com.gz.kidsafe
```

### Smoke assertions (policy-critical)
1. App process exists after launch.
2. Logcat shows ad init blocked when age is unknown (fail-closed default):
```bash
adb logcat -d | grep -E "KidSafeAdsManager|AdEligibilityGuard|AGE_UNKNOWN"
```
Expected signal:
- `Ads initialization blocked (reason=AGE_UNKNOWN)`

3. (Optional) Verify flag-gated adapter path by running a flag-enabled variant and checking init log only appears when all policy gates pass.

---

## 4) Feature-Flag Guard Verification Checklist

Code checkpoints:
- `AdEligibilityGuard.evaluate(...)`
  - blocks `AGE_UNKNOWN`
  - blocks when `MOBILE_ADS_REAL_ADAPTER_ENABLED=false`
- `KidSafeAdsManager.initialize(...)`
  - returns early with block reason unless guard allows
- `MainActivity` currently passes `AgeSignal.UNKNOWN` to banner surface for strict default no-ads path

---

## 5) Failure Triage Matrix

### A) Gradle/AGP incompatibility
Symptoms:
- Build fails during configuration with plugin/tooling errors.

Action:
- Confirm Gradle is pinned to `8.7`
- Keep setup-gradle cache disabled while stabilizing
- Re-run with `--stacktrace`

### B) Android SDK packages missing
Symptoms:
- `failed to find Build Tools` / missing platform errors.

Action:
- Install required packages via `sdkmanager`
- Accept licenses (`yes | sdkmanager --licenses`)

### C) Emulator boot timeout
Symptoms:
- `adb wait-for-device` hangs / boot not completed.

Action:
- Ensure KVM availability
- Switch to `-gpu swiftshader_indirect`
- Increase boot timeout and poll:
```bash
adb shell getprop sys.boot_completed
```

### D) Runtime crash on launch
Symptoms:
- PID absent, `am start` returns failure.

Action:
- Collect:
```bash
adb logcat -d > smoke-logcat.txt
```
- Attach crash stack + commit SHA to task thread.

---

## 6) Evidence Bundle to attach per lane run

- Commit SHA
- CI run URL
- `assembleDebug` terminal tail (success/failure)
- Unit test summary
- Runtime smoke output (`pidof`, logcat grep for `AGE_UNKNOWN` block)
- If failed: failing step + stacktrace excerpt + ETA for fix

---

## 7) Current ETA protocol

- Compile lane rerun verdict: **~15 min** from trigger
- Runtime smoke verdict on emulator lane: **~20–30 min** once compile artifact is green
