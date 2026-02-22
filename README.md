# KidSafe Android Prototype (_07)

Android prototype focused on **COPPA + Google Play Families compliance by design** and a safety-first AdMob integration.

## What this includes

- Kotlin Android app scaffold (`minSdk 24`, `targetSdk 34`)
- Child-directed defaults enforced in code at startup
- AdMob SDK integration with:
  - Child-directed + under-age tags enabled
  - Max content rating set to **G**
  - Non-personalized ad request signal (`npa=1`)
  - Runtime ad kill-switch (`BuildConfig.KIDSAFE_ADS_ENABLED`)
- No dangerous permissions and attempt to remove AD_ID from merged manifest
- A basic parental gate utility for any external-link surfaces
- Policy and risk documentation in `docs/CHECKPOINT_24H.md`

## Build

```bash
# From android-kidsafe-prototype/
./gradlew assembleDebug
./gradlew testDebugUnitTest
```

> Note: This repository is scaffolded in this environment and should be built in an Android SDK-enabled CI/local machine.

## AdMob test IDs

This prototype uses Google test ad unit IDs by default. Replace with production IDs only after policy review.

## Key files

- `app/src/main/java/com/gz/kidsafe/ads/AdPolicyConfig.kt`
- `app/src/main/java/com/gz/kidsafe/ads/KidSafeAdsManager.kt`
- `app/src/main/java/com/gz/kidsafe/MainActivity.kt`
- `docs/CHECKPOINT_24H.md`
