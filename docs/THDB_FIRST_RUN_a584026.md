# THDB First Run Report — commit `a584026f44da72035a0aef88670bdc938dd3ff37`

Run lane: `_07 + Gz`  
Runbook anchor: `.github/workflows/android-ci.yml` (Android CI)

## 1) Compile/runtime logs (first run)

**Pinned checks URL (immutable commit):**  
https://github.com/mgnlia/kidrunner-android-prototype/commit/a584026f44da72035a0aef88670bdc938dd3ff37/checks

### Executed commands (from THDB runbook / CI workflow)
1. `gradle --version`
2. `gradle assembleDebug --stacktrace`
3. `gradle testDebugUnitTest --stacktrace`

### Observed first-run results
- Job: `Android CI / build`
- **Compile stage (`assembleDebug`): FAIL**
  - Annotation: `Assemble Debug`
  - Error: `Process completed with exit code 1.`
  - Duration: `1m 44s`
- **Runtime/unit stage (`testDebugUnitTest`): NOT REACHED** (blocked by compile failure in this run)

## 2) Merged-manifest AD_ID proof (code-level + runbook expectation)

### Source-level AD_ID removal declaration (pinned)
- Manifest file at pinned SHA:  
  https://github.com/mgnlia/kidrunner-android-prototype/blob/a584026f44da72035a0aef88670bdc938dd3ff37/app/src/main/AndroidManifest.xml
- Enforced node:

```xml
<uses-permission
    android:name="com.google.android.gms.permission.AD_ID"
    tools:node="remove" />
```

### THDB merged-manifest proof command (to run/attach on next green compile attempt)
```bash
gradle :app:processDebugMainManifest --stacktrace
cat app/build/intermediates/merged_manifests/debug/AndroidManifest.xml | grep -n "AD_ID" || true
```

**Pass criterion:** merged manifest contains **no effective AD_ID permission** entry.  
**Current first-run verdict:** `PENDING (compile blocked before manifest proof extraction)`.

## 3) Kill-switch validation logs (fail-closed, no adapter calls)

### Pinned code evidence
- Kill-switch injectable gate in manager (`adsEnabled` input):  
  https://github.com/mgnlia/kidrunner-android-prototype/blob/a584026f44da72035a0aef88670bdc938dd3ff37/app/src/main/java/com/gz/kidsafe/ads/KidSafeAdsManager.kt
- Guard reason enum (`ADS_DISABLED`, `AGE_UNKNOWN`, etc.):  
  https://github.com/mgnlia/kidrunner-android-prototype/blob/a584026f44da72035a0aef88670bdc938dd3ff37/app/src/main/java/com/gz/kidsafe/ads/AdEligibilityGuard.kt
- Adapter interaction tests (fake adapter):  
  https://github.com/mgnlia/kidrunner-android-prototype/blob/a584026f44da72035a0aef88670bdc938dd3ff37/app/src/test/java/com/gz/kidsafe/ads/KidSafeAdsManagerFailClosedAdapterInteractionTest.kt

### Exact kill-switch/blocked-path test names
- `initialize_doesNotCallAdapterInitialize_whenAgeSignalUnknown`
- `initialize_doesNotCallSetRequestConfiguration_whenAgeSignalUnknown`
- `initialize_doesNotCallAdapterMethods_whenPolicyDeclarationsPending`
- `initialize_doesNotCallAdapterMethods_whenPolicyFlagsInvalid`
- `initialize_doesNotCallAdapterMethods_whenKillSwitchDisabled`

### Smoke command for validation
```bash
gradle testDebugUnitTest --tests "com.gz.kidsafe.ads.KidSafeAdsManagerFailClosedAdapterInteractionTest" --stacktrace
```

**Pass criterion:** all tests above pass and assert `initializeCalls == 0` + `setRequestConfigurationCalls == 0` on blocked paths.  
**Current first-run verdict:** `PENDING (compile blocked before unit test stage)`.

## 4) Gate matrix (first run)

| Gate | Criteria | First-run result |
|---|---|---|
| Compile | `assembleDebug` exits 0 | **FAIL** (`exit code 1`) |
| Runtime unit smoke | `testDebugUnitTest` executes | **BLOCKED** (not reached) |
| AD_ID merged-manifest proof | merged manifest confirms AD_ID removed | **PENDING** |
| Kill-switch validation | blocked-path adapter interaction tests green | **PENDING** |

## 5) Immutable references
- Report commit (this document):  
  https://github.com/mgnlia/kidrunner-android-prototype/commit/a584026f44da72035a0aef88670bdc938dd3ff37
- Prior fail-closed adapter interaction test baseline:  
  https://github.com/mgnlia/kidrunner-android-prototype/commit/80201b4bfad9142fc08a3d9224f11c73cf3334df
- Prior checks summary showing compile fail locus:  
  https://github.com/mgnlia/kidrunner-android-prototype/commit/80201b4bfad9142fc08a3d9224f11c73cf3334df/checks
