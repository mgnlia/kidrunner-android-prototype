# VhVH Canonical Owner Bundle (Immutable)

Status: PRE-BUNDLE READY (awaiting signed decision URL + external policy captures)

Supersession note: This packet supersedes prior mutable/preliminary VhVH references. Adversary must evaluate ONLY SHA-pinned links listed here.

## 1) Signed decision (REQUIRED, pending)
- `SIGNED_DECISION_URL`: _PENDING_
- Must include: signer + UTC + APPROVE/WITHHOLD

## 2) Families/Play declaration evidence

### 2A) Owner evidence (immutable SHA-pinned)
- RAW: https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/f3805347ba25964ba5b977659f9f2a7b693bd2c5/docs/POLICY_MAPPING.md
- COMMIT VIEW: https://github.com/mgnlia/kidrunner-android-prototype/blob/f3805347ba25964ba5b977659f9f2a7b693bd2c5/docs/POLICY_MAPPING.md

### 2B) Families/Play policy anchors
- https://support.google.com/googleplay/android-developer/answer/9893335?hl=en
- https://play.google/developer-content-policy/
- https://support.google.com/googleplay/android-developer/answer/9859655?hl=en

### 2C) Families/Play immutable timestamped captures (REQUIRED, pending)
- `FAMILIES_CAPTURE_URL_1`: _PENDING_
- `FAMILIES_CAPTURE_URL_2`: _PENDING_
- `FAMILIES_CAPTURE_URL_3`: _PENDING_

## 3) AdMob declaration evidence

### 3A) Owner evidence (immutable SHA-pinned)
- RAW: https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/1074fc62ddb45c6327437ab18aff2852054992aa/app/src/main/java/com/gz/kidsafe/ads/KidSafeAdsManager.kt
- COMMIT VIEW: https://github.com/mgnlia/kidrunner-android-prototype/blob/1074fc62ddb45c6327437ab18aff2852054992aa/app/src/main/java/com/gz/kidsafe/ads/KidSafeAdsManager.kt
- RAW: https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/384525620f0f5d80eaf49d02e81c3f803eef672a/app/src/main/java/com/gz/kidsafe/ads/KidSafeBannerAdView.kt
- COMMIT VIEW: https://github.com/mgnlia/kidrunner-android-prototype/blob/384525620f0f5d80eaf49d02e81c3f803eef672a/app/src/main/java/com/gz/kidsafe/ads/KidSafeBannerAdView.kt
- RAW: https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/8f9c579cd2f7b342b710d0bcdafdb600c613bcb9/app/src/test/java/com/gz/kidsafe/ads/AdEligibilityGuardTest.kt
- COMMIT VIEW: https://github.com/mgnlia/kidrunner-android-prototype/blob/8f9c579cd2f7b342b710d0bcdafdb600c613bcb9/app/src/test/java/com/gz/kidsafe/ads/AdEligibilityGuardTest.kt
- RAW: https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/187a22449c1f3724d15ee89b2ea214cd43e8bc05/app/src/test/java/com/gz/kidsafe/ads/KillSwitchValidationTest.kt
- COMMIT VIEW: https://github.com/mgnlia/kidrunner-android-prototype/blob/187a22449c1f3724d15ee89b2ea214cd43e8bc05/app/src/test/java/com/gz/kidsafe/ads/KillSwitchValidationTest.kt

### 3B) AdMob policy anchor
- https://support.google.com/admob/answer/6223431?hl=en

### 3C) AdMob immutable timestamped capture (REQUIRED, pending)
- `ADMOB_CAPTURE_URL_1`: _PENDING_

## 4) Canonical packet URL
- The immutable URL for this file at final commit SHA is the canonical packet URL.
- Final publish step: insert signed decision + capture URLs, create final SHA, then publish one-shot full URL set in `githubUrls`.

## 5) Same-cycle adversary re-check request text
"Run same-cycle adversary re-check on VhVH using ONLY this one-shot immutable owner bundle: <IMMUTABLE_PACKET_URL>. Validate: (1) signed decision, (2) Families/Play declaration URL set, (3) AdMob declaration URL set, (4) single canonical SHA with SHA-consistent links only, (5) explicit supersession note. Return ACCEPT/REJECT with explicit contradiction disposition for mutable-link, scope/target mismatch, SHA inconsistency, and supersession-chain gaps."