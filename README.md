# FitHome - Native Android Neumorphic Fitness App

FitHome is a high-fidelity native Android fitness application built with modern Jetpack Compose. It features a pixel-exact neumorphic design system ported directly from high-end UI specifications, complete with multi-layered soft shadows, custom Canvas dial controls, curved architectural surfaces, animated toggles, and seamless offline data persistence.

---

## Features
- **Neumorphic Design System**: Custom multi-layered soft shadows, raised cards, inset wells, gradient highlights, and responsive scaling.
- **Interactive Intensity Dial**: Custom-rendered Canvas thermostat-style intensity dial with 19 mirrored radial tick marks, dual glowing gradient arcs (warm/cool), haptic feedback, and touch drag gestures.
- **Weather Illustration & Outdoor Insights**: Custom Canvas sun-and-cloud vector rendering with atmospheric radial glow, soft shadows, and metrics strip (Sensible temperature, Humidity, Pressure).
- **Workout Tracking & Sessions**: Real-time workout session countdown with dynamic MET-based calorie calculation (`kcal = MET(intensity) * weightKg * hours`), live Pause/Resume, Stop confirmation, and Room persistence.
- **Hydration Tracker**: Instant quick-log hydration action (+250 ml) with animated visual feedback and DataStore persistence.
- **Dashboard & Workouts Grid**: Fluid vertical scrolling grid featuring custom dual-state neumorphic cards (OFF/ON toggle with 22px sliding knob), customizable workouts, and a fixed bottom dock.
- **Modular Architecture**: MVVM architecture, Navigation Compose, Kotlin 2.0, Jetpack Compose BOM, Room DB with auto-seeding, and Preferences DataStore.
- **100% Offline**: Zero external network dependencies, privacy-first, fully operational on airplane mode.

---

## Tech Stack
- **Language**: Kotlin 2.0.20
- **Build System**: Android Gradle Plugin 8.5.2, Gradle 8.8
- **UI Toolkit**: Jetpack Compose (BOM 2024.09.03), Material 3
- **Architecture**: MVVM with StateFlow & Coroutines
- **Navigation**: Navigation Compose 2.8.2
- **Persistence**: Room 2.6.1 (with KSP 2.0.20-1.0.25) & Jetpack DataStore Preferences 1.1.1
- **Min SDK**: API 26 (Android 8.0 Oreo)
- **Target SDK**: API 34 (Android 14)

---

## Font
The app uses **Vazirmatn** (OFL), bundled in `app/src/main/res/font/` (`vazirmatn_medium.ttf`, `vazirmatn_bold.ttf`). Nothing to download.

## Building the Project
### Local Build
```bash
gradle assembleDebug
```
The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### GitHub Actions CI/CD
A GitHub Actions workflow is provided at `.github/workflows/build-apk.yml`. It runs automatically on push and produces the debug APK as an artifact named `FitHome-debug-apk`.
