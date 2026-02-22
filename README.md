# Android Prototype App (Track B Execution)

This is the execution scaffold for the kids game prototype focused on:
- fast playable loop
- low-end performance
- COPPA/Google Play Families compliant ad architecture by design

## Current implementation status
- [x] Project skeleton (Gradle/Kotlin)
- [x] Core game loop scaffolding (`SurfaceView` + fixed-step thread)
- [x] Basic world update/render placeholders
- [x] Kid-safe ad policy abstraction (`KidSafeAdPolicy`)
- [x] Ad controller structure with no-ad fallback path (`KidSafeAdController`)
- [ ] Core gameplay entities complete
- [ ] Real Mobile Ads SDK wiring + integration test build
- [ ] Internal APK smoke test run

## Package layout
- `game/`: loop, world state, rendering, input hooks
- `ads/`: kid-safe ad policy + integration controller

## Compliance design stance
Default mode is **child-safe**. Any ad request path must pass policy checks first.
If policy constraints are not met, the app falls back to **no-ad mode**.
