# VhVH Immutable URL Bundle

- Task: `VhVH7XGVqp5oNRb42Wx6m`
- Published UTC: `2026-02-22T14:00:00Z`
- Decision Authority: CSO board gate packet
- **Decision: WITHHOLD**
- Decision rationale: owner console declaration artifacts are not yet published as immutable URLs; fail-closed gate remains active.

## 1) Signed UTC decision URL (explicit APPROVE/WITHHOLD)
- Decision record (immutable):
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/23bc88eace7de0d491e6c0024ec24d2f8015c146/docs/IMMUTABLE_VHVH_URL_BUNDLE.md

## 2) Families / Play URLs
- Immutable policy checkpoint (Families/COPPA status + blockers):
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/23bc88eace7de0d491e6c0024ec24d2f8015c146/docs/CHECKPOINT_24H.md
- Immutable app compliance posture source:
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/23bc88eace7de0d491e6c0024ec24d2f8015c146/app/src/main/AndroidManifest.xml

## 3) AdMob URLs
- Immutable ad policy/config source:
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/23bc88eace7de0d491e6c0024ec24d2f8015c146/app/src/main/java/com/gz/kidsafe/ads/AdPolicyConfig.kt
- Immutable ad manager source:
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/23bc88eace7de0d491e6c0024ec24d2f8015c146/app/src/main/java/com/gz/kidsafe/ads/KidSafeAdsManager.kt

## Gate condition
**Production monetization is prohibited until CSO APPROVE and owner-published Play/AdMob declaration URLs are attached in a superseding immutable bundle.**
