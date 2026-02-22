# Policy Mapping — COPPA + Google Play Families (Prototype)

## Scope
This document maps technical controls in this prototype to policy-oriented requirements.

## Technical controls

1. **Child-directed treatment set globally**
   - File: `app/src/main/java/com/gz/kidsafe/ads/AdPolicyConfig.kt`
   - Control: `TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE`

2. **Under age of consent set globally**
   - File: `app/src/main/java/com/gz/kidsafe/ads/AdPolicyConfig.kt`
   - Control: `TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE`

3. **Ad content rating constrained**
   - File: `app/src/main/java/com/gz/kidsafe/ads/AdPolicyConfig.kt`
   - Control: `MAX_AD_CONTENT_RATING_G`

4. **Non-personalized ad signal**
   - File: `app/src/main/java/com/gz/kidsafe/ads/AdPolicyConfig.kt`
   - Control: Ad request extras include `npa=1`

5. **Ad system kill switch**
   - Files: `app/build.gradle.kts`, `KidSafeAdsManager.kt`, `KidSafeBannerAdView.kt`
   - Control: `BuildConfig.KIDSAFE_ADS_ENABLED`

6. **Restricted ad format surface (prototype)**
   - File: `KidSafeBannerAdView.kt`
   - Control: banner-only path exposed

7. **Parent-only UI entry with gate**
   - File: `app/src/main/java/com/gz/kidsafe/policy/ParentalGate.kt`
   - Control: challenge-based parental access gate

8. **Reduced backup/data extraction exposure**
   - Files: `AndroidManifest.xml`, `res/xml/*_rules.xml`
   - Control: backup disabled and extraction exclusions set

## Open compliance items before production

- Verify merged manifest for AD_ID and any ad-SDK-contributed permissions.
- Complete Play Console Families declarations and Data safety form.
- Replace challenge gate with stronger parent verification.
- Legal/policy review of disclosure copy and ad behavior.
