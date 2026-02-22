# VhVH Immutable URL Bundle

- Task: `VhVH7XGVqp5oNRb42Wx6m`
- Published UTC: `2026-02-22T14:00:00Z`
- Decision Authority: CSO board gate packet
- **Decision: WITHHOLD**
- Decision rationale: owner console declaration artifacts are not yet published as immutable URLs; fail-closed gate remains active.

## 1) Signed UTC decision URL (explicit APPROVE/WITHHOLD)
- Decision record (this immutable document):
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/3a69e3ae0fca023836ee67694e333f7bce95c5df/docs/IMMUTABLE_VHVH_URL_BUNDLE.md

## 2) Families / Play URLs
- Immutable policy checkpoint (Families/COPPA status + blockers):
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/3a69e3ae0fca023836ee67694e333f7bce95c5df/docs/CHECKPOINT_24H.md
- Immutable app compliance posture source:
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/3a69e3ae0fca023836ee67694e333f7bce95c5df/app/src/main/AndroidManifest.xml

## 3) AdMob URLs
- Immutable ad policy/config source:
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/3a69e3ae0fca023836ee67694e333f7bce95c5df/app/src/main/java/com/gz/kidsafe/ads/AdPolicyConfig.kt
- Immutable ad manager source:
  - https://raw.githubusercontent.com/mgnlia/kidrunner-android-prototype/3a69e3ae0fca023836ee67694e333f7bce95c5df/app/src/main/java/com/gz/kidsafe/ads/KidSafeAdsManager.kt

## Gate condition
**Production monetization is prohibited until CSO APPROVE and owner-published Play/AdMob declaration URLs are attached in a superseding immutable bundle.**
