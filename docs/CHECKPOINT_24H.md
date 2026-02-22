# 24h Checkpoint — _07 Android Prototype + Gz AdMob Kid-Safe Integration

## 1) Build status (current)

**Status:** Scaffold implemented, pending Android SDK-enabled CI/local build execution.

Implemented:
- Kotlin Android app skeleton with Compose UI
- Centralized kid-safe ad policy config (`AdPolicyConfig`)
- AdMob initialization guard + kill switch (`KidSafeAdsManager`)
- Kid-safe banner integration using Google test IDs (`KidSafeBannerAdView`)
- Minimal parental gate for parent-only surfaces (`ParentalGate`)
- Unit tests for parental gate validator + ad policy config constants

Pending verification in Android build environment:
- `./gradlew assembleDebug`
- `./gradlew testDebugUnitTest`
- Manifest merge audit to confirm `AD_ID` removal behavior with current GMA dependency

## 2) Ad integration approach (Families + COPPA by design)

Design principles:
1. **Policy defaults immutable at startup**
   - Always set:
     - `TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE`
     - `TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE`
     - `MAX_AD_CONTENT_RATING_G`
2. **Non-personalized ads only**
   - Adds `npa=1` extra to ad requests
3. **Safe format subset**
   - Prototype uses banner only, no reward/interstitial/open-ad path exposed
4. **Runtime kill switch**
   - `BuildConfig.KIDSAFE_ADS_ENABLED` can hard-disable all ad loading
5. **No broad data access**
   - No dangerous permissions requested
   - Attempt removal of AD_ID from manifest merge (needs final merge report validation)
6. **Parent-only controls behind parental gate**
   - External-link / settings surfaces intended to remain parent-gated

## 3) Risk list

1. **AD_ID merge behavior**
   - Google Mobile Ads may contribute AD_ID depending on version/manifest merge.
   - Mitigation: explicit `tools:node="remove"`, inspect merged manifest in CI.

2. **Families policy interpretation drift**
   - Policy text evolves; ad format/content restrictions can change.
   - Mitigation: maintain policy mapping doc + quarterly compliance review.

3. **Mediation adapters accidental inclusion**
   - Third-party mediation could weaken compliance posture if not controlled.
   - Mitigation: no mediation enabled in prototype; explicit allowlist if introduced.

4. **Parental gate strength**
   - Math-gate is baseline and may be insufficient for strong assurance.
   - Mitigation: upgrade to robust parent verification flow for production.

5. **Telemetry/privacy controls incomplete**
   - Analytics SDKs not yet integrated; future additions may introduce data collection risk.
   - Mitigation: privacy budget + SDK DPIA gate before adding any telemetry.

## 4) Blockers

1. **No Android SDK/Gradle runtime in current environment**
   - Cannot execute build/test commands here.

2. **Production identifiers unavailable**
   - Real AdMob app/ad unit IDs and Play Console policy declarations pending.

3. **Policy/legal sign-off pending**
   - Families listing metadata, Data safety form, and ad disclosure text need review.

## Next 24h execution plan

- Run full debug build + unit tests on Android runner
- Export merged manifest and dependency tree artifacts
- Add instrumentation smoke test for ad container lifecycle
- Draft release checklist: Families declaration + COPPA evidence matrix
