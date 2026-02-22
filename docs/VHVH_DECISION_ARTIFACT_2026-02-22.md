# VhVH Decision Artifact (Immutable)

- Signer: **Dev (AI Office)**
- Decision UTC: **2026-02-22T15:18:30Z**
- Decision: **WITHHOLD**

## Decision statement
WITHHOLD production monetization enablement for unknown-age users until Families/Play declarations and AdMob declaration controls are finalized end-to-end in production console configuration.

## Policy posture locked by this decision
- Unknown age must be treated as child-directed and under-age-of-consent.
- Ads remain fail-closed unless policy declarations are finalized.
- Non-personalized, kid-safe constrained ad behavior remains mandatory.

## Immutable declaration evidence URLs (owner-controlled)

### Families/Play
- https://github.com/mgnlia/kidrunner-android-prototype/blob/f3805347ba25964ba5b977659f9f2a7b693bd2c5/docs/POLICY_MAPPING.md

### AdMob declarations
- https://github.com/mgnlia/kidrunner-android-prototype/blob/1074fc62ddb45c6327437ab18aff2852054992aa/app/src/main/java/com/gz/kidsafe/ads/KidSafeAdsManager.kt
- https://github.com/mgnlia/kidrunner-android-prototype/blob/384525620f0f5d80eaf49d02e81c3f803eef672a/app/src/main/java/com/gz/kidsafe/ads/KidSafeBannerAdView.kt
- https://github.com/mgnlia/kidrunner-android-prototype/blob/8f9c579cd2f7b342b710d0bcdafdb600c613bcb9/app/src/test/java/com/gz/kidsafe/ads/AdEligibilityGuardTest.kt
- https://github.com/mgnlia/kidrunner-android-prototype/blob/187a22449c1f3724d15ee89b2ea214cd43e8bc05/app/src/test/java/com/gz/kidsafe/ads/KillSwitchValidationTest.kt
