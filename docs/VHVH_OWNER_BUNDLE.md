# VhVH Canonical Owner Bundle (Immutable)

Status: PRE-BUNDLE SKELETON (awaiting signed decision URL)

## Required One-Shot Evidence URLs

### 1) Signed decision (REQUIRED, pending)
- `SIGNED_DECISION_URL`: _PENDING_  
  Must include: signer + UTC + APPROVE/WITHHOLD

### 2) Families/Play declarations (immutable)
- https://github.com/mgnlia/kidrunner-android-prototype/blob/f3805347ba25964ba5b977659f9f2a7b693bd2c5/docs/POLICY_MAPPING.md

### 3) AdMob declarations/evidence (immutable)
- https://github.com/mgnlia/kidrunner-android-prototype/blob/1074fc62ddb45c6327437ab18aff2852054992aa/app/src/main/java/com/gz/kidsafe/ads/KidSafeAdsManager.kt
- https://github.com/mgnlia/kidrunner-android-prototype/blob/384525620f0f5d80eaf49d02e81c3f803eef672a/app/src/main/java/com/gz/kidsafe/ads/KidSafeBannerAdView.kt
- https://github.com/mgnlia/kidrunner-android-prototype/blob/8f9c579cd2f7b342b710d0bcdafdb600c613bcb9/app/src/test/java/com/gz/kidsafe/ads/AdEligibilityGuardTest.kt
- https://github.com/mgnlia/kidrunner-android-prototype/blob/187a22449c1f3724d15ee89b2ea214cd43e8bc05/app/src/test/java/com/gz/kidsafe/ads/KillSwitchValidationTest.kt

## Canonical Packet URL
- This file URL at commit SHA is the canonical packet URL.
- Final publish step: replace `SIGNED_DECISION_URL` with immutable URL, then post one-shot githubUrls and request adversary re-check in same cycle.
