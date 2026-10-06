# Handoff: akshitnahata.com redesign ("Material You playground")

## Overview
A single-page, playful Material You portfolio for Akshit Nahata, Android Engineer at Jaguar Land Rover. One long scrolling page: Hero, Work, How I build, Career, Contact, Footer. Overlays: Quick Settings shade, toasts and snackbars, first-visit boot intro, 404 ANR dialog, Developer options easter egg. Visitors pick a "wallpaper" seed colour and the whole site re-themes (dynamic colour), in light or dark.

Target stack: **Kotlin Multiplatform + Compose Multiplatform for Web (wasmJs, canvas rendered), Material 3, hosted on Netlify.** Every effect was chosen to be implementable with Compose primitives. Compose mappings are given throughout.

## About the design files
The files in this bundle are **design references built in HTML**. They are prototypes that show the intended look and behaviour. They are not production code to copy. The job is to **recreate them in Compose Multiplatform** using MaterialTheme, Material 3 components and Compose animation APIs. Open `Portfolio.dc.html` in a browser for the live reference (it needs `support.js` next to it). `Hero Directions.dc.html` is the review board with every state frozen (turn 2, ids 2a to 2u).

## Fidelity
**High fidelity.** Final colours, type, spacing, radii, copy and motion. Recreate it closely.

## Copy rules (important)
- First person, warm, cheeky, specific. **No em or en dashes anywhere in copy.** Use commas or full stops.
- The title is "Android Engineer" (never "Senior"). Spell "Jaguar Land Rover" in full. No JLR or Range Rover logos.
- The Range Rover App CTA is always "View on Google Play", never "View source".
- No CV download.
- No education entry.

## Layout system
- Fluid from 390 to 1440+. Max content width **1328**, side margins clamp(16, 4% of width, 56). The breakpoint for structural changes is **width < 760 = compact** (WindowSizeClass Compact vs Medium/Expanded).
- Font sizes use clamp(min, x% of container width, max), so implement them as a linear interpolation on window width between 390 and 1440, clamped.
- Section top padding clamp(72, 9%, 140).

## Screens / sections

### 0. Status strip and app bar (top)
- 40dp strip: left "9:41" (JetBrains Mono 12), centre pill button "pull for quick settings" (compact: "pull"), surfaceContainer bg, a 28x4 outline handle, which opens Quick Settings. Right: Gradle build text `:app:assembleRelease {pct}%` (compact: `{pct}%`), which becomes **"BUILD SUCCESSFUL in 4s"** at 100%.
- 4dp progress track (surfaceContainer) with a primary fill = scroll fraction.
- App bar: logo (34dp primary rounded-12 square holding an 18x10 dark pill) + "akshit" + ".kt" in primary (Bricolage 700 22). Centre (expanded only): segmented nav pill (surfaceContainerLow, 6 padding, radius full) with items Work (selected: secondaryContainer), How I build, Career, Contact. Clicking scrolls to the section (animateScrollTo). Right: 52dp Quick Settings button (radius 18, surfaceContainer, 2x2 grid of 12dp shapes, first one primary).
- Compact: the nav becomes a horizontal scrolling chip row under the app bar.

### 1. Hero
- Label (mono, primary): `0x01 // BOOT_COMPLETE` + " (took one coffee)" in onSurfaceVariant.
- Name: "AKSHIT" / "NAHATA" (second line primary). Bricolage Grotesque 800, size clamp(76, 11.2%, 164), line-height 0.84, letter-spacing -0.05em.
- Chips: [dot] "Android Engineer at Jaguar Land Rover" (surfaceContainer, 600) and "Manchester, UK" (1.5dp outline).
- Tagline (Figtree 500, clamp(19, 1.8%, 26), lh 1.38, onSurfaceVariant, max 660): "I build Android apps that feel effortless on the outside and are beautifully engineered on the inside."
- CTAs, height clamp(56, 4.4%, 64), radius full, Figtree 700 18: filled primary "See my work →" (scrolls to Work) and tonal secondaryContainer "Get in touch" (scrolls to Contact). Hover: scale 1.04, radius morphs from full to 22 (expressive spring).
- Right stage (expanded only, square clamp(380, 34%, 480)): primaryContainer blob (asymmetric 44/56/52/48 % radii, use a RoundedPolygon), tertiaryContainer rounded-36 square rotated 14°, tertiary circle, a speech bubble that cycles every 5s ("Hi, I'm Bit. Poke me." / "Psst. Pull the status bar." / "I run on coffee and coroutines."), and Bit at 250dp. Compact: Bit at 64dp in a 96dp primaryContainer rounded-34 badge at the top right of the label row.
- Stat tiles (adaptive grid, min 150): 
  1. TARGET_SDK **36**, "Always the latest.", surfaceContainer r28. It counts up from 0 on load and tap replays it.
  2. CORE_STACK **Kotlin**, "Fluent. No accent.", primaryContainer, radius 999/28/28/999.
  3. UI_ENGINE **Compose**, "Zero XML harmed.", tertiaryContainer, radius 28/28/64/28.
  4. COFFEE_PER_RELEASE **3**, "Tap to refill.", inverseSurface. Tap: +1, squash, toast cycles: "Coffee level: optimal." / "Release confidence +12%." / "Gradle is still faster than the kettle." / "Okay, that's a lot of coffee." / "Shipping to production. On caffeine."
  5. APPS_ON_PLAY **4**, "Personal, and counting.", secondaryContainer, radius 28/64/28/28. Tap scrolls to the More on Play row.
  - Hover: translateY -6, rotate ±1°.

### 2. Work
Header: `0x02 // SELECTED_REPOS (the ones I show my mum)`, H2 "Things I've shipped" (Bricolage 800 clamp(44, 5.6%, 80)), sub "Four apps of my own on Google Play, and one that lives inside some very nice cars."

**Vantage Point (flagship)**: surfaceContainerLow card, radius clamp(32, 3.4%, 48), padding clamp(22, 3.4%, 48), two columns that wrap (min 440 / 420).
- Chips: FLAGSHIP (primary), "Live on Google Play", "★ 5.0 rating" (tertiaryContainer).
- Title "Vantage Point" (clamp(48, 6%, 88)). One-liner: "Bury a memory at a real place. A time capsule you find, not scroll."
- 4 highlight tiles (r24): "On-device E2E encryption" / "Per-capsule keys in Android Keystore, with recovery phrase escrow." · "Scan an object to unlock" / "ML Kit recognition with a composite scoring model." · "No background location" / "A custom geometry engine, while-in-use only." · "Server-enforced time locks" / "Cloud Functions decide when a capsule opens. Not your phone's clock."
- Buttons: "View on Google Play" (https://play.google.com/store/apps/details?id=com.akshit.vantagepoint) and a tonal toggle "Flip to spec sheet" / "Back to screenshots".
- Right column: two phones in black frames (radius 38, padding 8, aspect 9:19.5, width clamp(190, 18%, 260)), rotated -3° and +3°. A LazyRow with snap when compact. **Cursor tilt**: rotateY up to ±5°, rotateX up to ±4° (graphicsLayer, spring back on exit).
- **Spec sheet** (AnimatedContent swap): a dark panel (hue-tinted) showing the module graph: `:app` → `:feature:home | :feature:recovery | :feature:sharing` → `:core:domain` (pure Kotlin) → `:core:data`, `:core:crypto`, `:core:geo`, `:core:billing` → Cloud Functions (TypeScript). Plus the CI line: "GitHub Actions running ktlint, detekt, JVM unit tests, Koin wiring tests and Firestore emulator tests. Red builds don't merge."

**Range Rover App**: always-dark premium card (Range Rover card bg #0c110c, text #eaf0ea, action tile #1b211b, action active #9be39d; Bit screen #0d1a0d, Bit eyes #a1f7a3 (all hue follows seed)).
- Chips ENTERPRISE, "Jaguar Land Rover". Title "Range Rover App" + mono "known internally as OneApp".
- Body: "The companion app for Range Rover Electric and Range Rover Sport Electric. Lock it, warm it up, find it, charge it, and track your order, all from your phone."
- MY_ROLE box: "I own remote connectivity: every action sent to the car and every bit of data coming back. I'm also the team's security champion, running threat models, API reviews and dependency scans."
- 4 quick actions (toggle; label, colour and shape morph from r20 to r28, dot morphs from square to circle): Unlock→Unlocked "Unlocked. Relax, it's not your car." · Beep→Beeped "Beep beep. Somewhere a neighbour sighs." · Climate→21°C "Pre-conditioning to 21°C. Toasty." · Locate→Found "Found it. Exactly where you parked it."
- CTA "View on Google Play" → https://play.google.com/store/apps/details?id=com.jaguarlandrover.rangerover.app
- The two store images are already framed. Show them as-is at radius 28, with tilt.

**Roamio** (tertiaryContainer card), next to **This portfolio** (primary card, bottom-left radius 120). They wrap.
- Roamio: "An offline-first travel planner. Your itinerary still works on the plane, in the tunnel, and in that one café with no signal." Outline chips: Firebase Auth, DataStore, Build flavours, Daily automated Play releases. Two framed phones. CTA → https://play.google.com/store/apps/details?id=com.akshit.roamio
- This portfolio: "You're looking at it. Yes, this is Kotlin." / "Kotlin Multiplatform and Compose for Web, rendered to a canvas, hosted on Netlify. Every bounce you've felt so far is a Compose spring()." A code block `fun main() = ComposeViewport { Portfolio(whimsy = true) }`. Button "Open Quick Settings".

**More on Play row**: PotterPedia (https://play.google.com/store/apps/details?id=com.akshit.potterpedia), HangMan, Find My Colors (both → https://play.google.com/store/apps/developer?id=Akshit+Nahata), and "See all on Google Play →".

### 3. How I build (home screen widgets)
Header `0x03 // HOW_I_BUILD (long press to rearrange)`, "How I build", "The boring bits, done properly, so the fun bits can ship."
Grid: adaptive columns (min 150), row height clamp(150, 12%, 176), dense packing, gap clamp(10, 1.2%, 16). Use LazyVerticalGrid with spans. Widgets in order (cols x rows):
| id | label | title | sub | span | bg / fg | radius |
|---|---|---|---|---|---|---|
| sec | SECURITY_CHAMPION | Security champion | Threat modelling. API reviews. Dependency vulnerability scanning. Someone has to be the friendly paranoid one. | 2x2 | dark hue-tinted | 40 |
| arch | ARCHITECTURE | MVVM and MVI | On Clean Architecture. Layers that stay in their lane. | 2x1 | primaryContainer | 36 |
| flow | ASYNC | Coroutines and Flow | Structured and cancellable. | 1x1 | tertiaryContainer | 32/32/64/32 |
| di | DI | Koin and Hilt | Whichever fits. | 1x1 | secondaryContainer | 80/80/32/32 |
| kmp | MULTIPLATFORM | Kotlin Multiplatform | Shared logic, native feel. Also: this website. | 2x1 | surfaceContainer | 999/36/36/999 |
| ktor | NETWORKING | Ktor | Typed clients everywhere. | 1x1 | surfaceContainerHigh | 32 |
| fb | BACKEND | Firebase | Auth, Firestore, Storage, Functions. | 1x1 | primaryContainer | 32/64/32/32 |
| test | TESTING | WireMock and friends | JVM unit tests, wiring tests, emulator tests. | 2x1 | inverseSurface | 36 |
| ci | CI_CD | GitHub Actions | ktlint and detekt on every PR. Red builds don't merge. | 2x1 | primary | 36/36/80/36 |
- A tap outside edit mode squashes the widget. **Long press 480ms** enters edit mode: toast "Edit mode. Tap two widgets to swap them.", all widgets wiggle ±1.4° (180 to 240ms, alternating, infinite), and a "Done" button appears. Tap one (it gets a 3dp primary ring with a 4dp surface gap), tap another, and they swap.

### 4. Career (Activity lifecycle)
Header `0x04 // RUNTIME_HISTORY`, "Career, as an Activity", "Every good app has a lifecycle. Here's mine, newest first. Lifecycle purists, look away." Header column left, timeline right (wraps on compact). One entry open at a time. The first is open by default.
1. `onResume()` · Apr 2023 to now · **RESUMED** badge · Android Engineer · Jaguar Land Rover, Manchester. Lines: "Building the Range Rover App in Kotlin, Compose and Clean Architecture." / "Own remote connectivity: every command to the car and every byte of data back." / "Team security champion: threat models, API reviews, dependency scans."
2. `onStart()` · Sep to Dec 2022 · Contract Android Developer · IDS Logic. Lines: "Built a frictionless checkout with Google One Tap and Google Pay, cutting checkout latency by 50%." / "Integrated ExoPlayer for adaptive streaming and rebuilt the in-app purchase pipeline." / "Fixed tricky production bugs while keeping the codebase modular and testable."
3. `onCreate()` · Oct 2021 to Aug 2022 · Android Engineer · Freelance. Lines: "Built and published Android apps for clients." / "Shipped my own apps to Google Play along the way."
- Node: 56dp. Closed: circle, surfaceContainer, outline dot. Open: radius 18, primary, onPrimary dot (shape morph). Card: open surfaceContainer, closed surfaceContainerLow, r32. Expand with animateContentSize (gentle spring). Ripple on the card.

### 5. Contact
A primaryContainer card, radius clamp(36, 4%, 56). Label `0x05 // SAY_HELLO`. Headline "Inbox open. Coffee ready. Let's build something." (Bricolage 800 clamp(48, 6.4%, 96), lh .9). Sub "Hiring, collaborating, or just want to talk Compose? I reply faster than a cold Gradle build."
- Email pill (surface bg): "account@akshitnahata.com" + a primary "Copy" chip. It copies to the clipboard and shows a snackbar "Email copied to clipboard" with action **Undo**, which shows "Undo failed. It's in your clipboard forever now, like a good commit."
- Outline pills: LinkedIn (https://www.linkedin.com/in/akshit-nahata), GitHub (https://github.com/ANAHATA6302), Instagram (https://www.instagram.com/ak__it/).
- Mono line "Based in Manchester, UK. Open to relocating."
- Photo (assets/akshit-sm.jpg), aspect 4:5, radius 48%/48%/40/40, object-position 50% 30%. Bit (wave pose, 84dp) in a surface rounded-36 badge overlapping the bottom right.

### 6. Footer
Bit (sleep pose, 44dp) + "© 2026 Akshit Nahata. Built with Compose Multiplatform." Right: a "Build number / 2026.10.06 (release)" button (surfaceContainerLow r16).
**Developer options easter egg:** tap Build number 7 times. From tap 3: "You are now N steps away from being a developer." At 7: toast "You are now a developer!", 90-piece confetti (primary, tertiary, containers), every Bit switches to the **dev** pose (shades and party hat), and the QS Developer options tile turns on ("Unlocked"; tapping it fires confetti again). Further taps show "No need, you are already a developer."

### Overlays
- **Quick Settings shade**: slides down from the top, width min(600, 100%), surfaceContainerLow, bottom radius 36, shadow 0 12 40 rgba(0,0,0,.18), scrim black 32%. Contents: big "9:41", mono "QUICK_SETTINGS // no root required", a 2x2 grid of pill tiles (min height 64; on: primary/onPrimary, off: surfaceContainerHigh) for Dark theme, Whimsy mode, Reduce motion and Developer options (Locked/Unlocked). Then "Wallpaper colour {name}" with 5 swatches (44dp circles; selected ring 3dp surfaceContainerLow + 6dp onSurface) and a drag handle. Every toggle toasts: "Dark theme on. Easy on the eyes." / "Light theme. Hello, sunshine." / "Whimsy off. Calm, professional, slightly sad." / "Whimsy back on. Springs restored." / "Reduce motion on. Everything just fades now." / "Motion back to normal." / "Wallpaper set to {name}. Re-theming everything." Locked dev tile: "Locked. Tap Build number 7 times. You know the drill."
- **Toast / snackbar**: bottom centre, 24dp up, inverseSurface, r14, min height 48, max width 560. Optional action uses the dark-scheme primary colour. Shows for 2.8s, or 4.2s with an action.
- **Boot intro** (first visit only, persisted flag, Skip button top right): Bit in loading pose in a 180dp primaryContainer r64 tile. Mono lines appear 330ms apart: "akshitOS 16 booting", "> loading coroutines... ok", "> inflating whimsy... ok", "> BOOT_COMPLETE". An 8dp progress bar fills over 1.5s. Total 1.75s, then it crossfades out.
- **404 ANR dialog**: scrim 42%, a Material 3 AlertDialog (r28, surfaceContainer, max 440): Bit in anr pose in a 120dp tile, mono "ERROR 404 // PAGE_NOT_FOUND", title "Akshit's Portfolio isn't responding", body "This page doesn't exist. Bit checked everywhere, even behind the sofa." Buttons: text "Wait" (toast "Still waiting. Bit is checking again.") and filled "Close app" (goes home).

### Bit (mascot)
Original droid, 120x140 base, built only from rounded rects and circles (draw with Canvas). Antenna: 4x20 onSurface stick + 18dp tertiary ball. Arms: 14x34 r7 onSurface at y56. Feet: 22x20. Body: 100x96 r36 primary with an 8dp inner bottom shade. Screen: 76x50 r20, dark tinted. Eyes: 11x18 r6 light tinted pills, 18 apart. Mouth: 14x6 smile. Cheeks: 12x6 tertiaryContainer.
Poses: **idle** · **wave** (right arm rotated -150°) · **loading** (three dots, opacity 1/.6/.3) · **sleep** (eyes become 14x4 lines, "zz") · **anr** (ring eyes, flat mouth, tertiary sweat drop) · **dev** (black shades, tertiary party hat with a primaryContainer pom).
Ambient: blink every 3.4s (scaleY 0.1 for 130ms). Eyes follow the pointer up to 6dp. Tap: jump 22dp + rotate -6° (expressive) and toast "Beep boop. I'm Bit." / "Stop poking, I'm compiling." / "Fun fact: I'm 100% Compose." / "Try the Work section. Totally unbiased." / "onPause() called. Just kidding."

## Motion
| Token | Compose | Web reference | Used for |
|---|---|---|---|
| expressive | spring(dampingRatio = 0.6f, stiffness = 380f) | cubic-bezier(.34,1.56,.64,1) 550ms | hover scale, shape morphs, QS shade, snackbar, Bit jump, tilt return |
| gentle | spring(0.8f, 200f) | ~700ms | scroll reveals, spec sheet swap, career expand |
| snappy | spring(1f, 1500f) | ~200ms | toggles, swatch ring, selection |
| colour | tween(400, EmphasizedEasing) | 400ms | all colour roles on re-theme (animateColorAsState) |
- Ripple on every clickable: 650ms, radius 2.2x the longest side, alpha 0.24 to 0 (ripple()).
- Scroll reveal: once, when 12% visible, fade in and rise 48dp with the gentle spring.
- Count-up: 0 to 36 over 1100ms, ease out cubic.
- **Whimsy off**: every spring becomes spring(1f, 500f) (~300ms, no overshoot). Wiggle, blink, eye tracking, tilt and confetti are disabled.
- **Reduce motion** (toggle or OS setting): everything becomes a 150ms fade. No translate, scale or rotate. The count-up jumps to its final value. Smooth scroll is off.

## State
`seed: Int (hue)`, `dark: Boolean` (default: system), `whimsy = true`, `reduceMotion` (default: OS), `qsOpen`, `devUnlocked` + `buildTaps`, `specOpen`, `editMode` + `selectedWidget` + `widgetOrder`, `careerOpen = 0`, `carActions: Set`, `coffee = 3`, `toast: (text, action?)`, `bootSeen` (persisted), `scrollFraction`. Persist seed, dark, whimsy and reduceMotion locally too (a nice touch).

## Design tokens

### Dynamic colour
Every role has a **fixed OKLCH lightness and chroma**. Only the hue comes from the seed (tertiary roles use seed hue + 70). That keeps contrast constant across seeds. Generate the scheme at runtime: convert OKLCH to sRGB, then build `lightColorScheme`/`darkColorScheme`. (MaterialKolor is an alternative, but it won't match exactly.)
| Role | CSS var | Light (L, C) | Dark (L, C) | Light hex @145 | Dark hex @145 |
|---|---|---|---|---|---|
| primary | --p | 0.5, 0.19 | 0.82, 0.13 | #007c00 | #8cda8f |
| onPrimary | --op | 0.99, 0.01 | 0.26, 0.09 | #f8fef8 | #002e02 |
| primaryContainer | --pc | 0.91, 0.09 | 0.38, 0.12 | #bcf2bd | #005211 |
| onPrimaryContainer | --opc | 0.27, 0.09 | 0.93, 0.06 | #003104 | #d0f3d0 |
| secondaryContainer | --sec | 0.92, 0.04 | 0.3, 0.04 | #d5ecd5 | #213321 |
| onSecondaryContainer | --osec | 0.28, 0.05 | 0.92, 0.03 | #182f19 | #d9ead9 |
| tertiary | --t | 0.52, 0.16 (hue+70) | 0.8, 0.12 | #007ca1 | #4cd1ee |
| tertiaryContainer | --tc | 0.9, 0.08 (hue+70) | 0.38, 0.1 | #9fecff | #004e63 |
| onTertiaryContainer | --otc | 0.28, 0.09 (hue+70) | 0.93, 0.06 | #003244 | #bbf3ff |
| surface | --s | 0.985, 0.008 | 0.165, 0.012 | #f7fcf7 | #0b100b |
| surfaceContainerLow | --sc1 | 0.96, 0.014 | 0.2, 0.015 | #ecf4ec | #121812 |
| surfaceContainer | --sc2 | 0.935, 0.02 | 0.235, 0.02 | #e2ede1 | #182118 |
| surfaceContainerHigh | --sc3 | 0.9, 0.03 | 0.28, 0.025 | #d2e4d2 | #212c21 |
| onSurface | --os | 0.21, 0.02 | 0.93, 0.012 | #131b13 | #e3eae3 |
| onSurfaceVariant | --osv | 0.43, 0.03 | 0.78, 0.025 | #465446 | #aebcae |
| outline | --ol | 0.72, 0.03 | 0.5, 0.03 | #99aa99 | #596859 |
| inverseSurface | --inv | 0.27, 0.02 | 0.92, 0.012 | #202920 | #e0e7e0 |
| inverseOnSurface | --oinv | 0.95, 0.01 | 0.22, 0.02 | #eaf0ea | #151d15 |

Seeds offered (swatch = oklch(.64 .19 h)):
| Seed | Hue | Swatch hex | Light primary | Dark primary |
|---|---|---|---|---|
| Android green (default) | 145 | #1fa836 | #007c00 | #8cda8f |
| Electric violet | 295 | #976ef1 | #6f41c1 | #cab3ff |
| Tangerine | 45 | #e55b00 | #b32a00 | #ffa87b |
| Ocean | 240 | #0096f2 | #0069c2 | #6dcfff |
| Bubblegum | 350 | #db509c | #ab1d72 | #ffa0d0 |

Default seed: Android green, hue 145 (user's pick #2e9e4a).
Check contrast for any new seed hue. Yellows (h about 90 to 110) are the weakest at L .5 primary.

### Typography
Fonts: **Bricolage Grotesque** (display, 800), **Figtree** (body 400 to 700), **JetBrains Mono** (system labels 500). All are Google Fonts. Bundle them as resources for Compose Web.
| Style | Font | Weight | Size (expanded / compact) | Line height | Tracking |
|---|---|---|---|---|---|
| displayLarge (hero name) | Bricolage | 800 | 164 / 76 | 0.84 | -0.05em |
| displayMedium (section H2) | Bricolage | 800 | 80 / 44 | 0.92 | -0.04em |
| displaySmall (card titles) | Bricolage | 800 | 88 / 48 (Vantage), 64 / 40 (others) | 0.9 | -0.045em |
| headlineMedium (career role) | Bricolage | 800 | 36 / 24 | 1.1 | -0.02em |
| stat value | Bricolage | 800 | 48 / 34 | 1.0 | -0.03em |
| titleLarge (tagline) | Figtree | 500 | 26 / 19 | 1.38 | 0 |
| bodyLarge | Figtree | 400 to 500 | 20 / 17 | 1.45 | 0 |
| bodyMedium | Figtree | 400 | 16 / 15 | 1.5 | 0 |
| labelLarge (buttons) | Figtree | 700 | 17 to 18 | 1 | 0 |
| labelSystem | JetBrains Mono | 500 | 14 / 12 (11 on tiles) | 1.3 | 0 |

### Shape
xs 12 (toast 14) · s 16 to 20 · m 28 (tiles, dialog) · l 36 to 40 (widgets) · xl 48 (cards) · full (buttons, chips, QS tiles). Asymmetric "playful" corners as listed per component. Hero blob: RoundedPolygon / Material 3 Expressive shapes.

### Spacing
4, 8, 12, 16 (compact margin), 24 (card gap), 32, 48 (card padding), 56 (expanded margin), 72 to 140 (section gap). Max width 1328.

### Elevation
Tonal: surface → containerLow → container → containerHigh. Shadows only on floating elements: shade 0 12 40 18%, snackbar 0 6 20 20%, dialog 0 20 60 30%, phones 0 30 60 -24 45%.

## Accessibility
- WCAG AA in both themes. Roles are lightness-locked for that reason.
- Every interactive element is focusable with a visible focus ring (add a 3dp primary outline that the HTML doesn't show).
- Targets are at least 44dp.
- Boot intro is skippable and shown once.
- Reduce motion and Whimsy off as above.
- Alt text on every screenshot.

## Assets
- `assets/vantage-home.jpg`, `assets/vantage-create.webp`: Vantage Point screenshots (user supplied).
- `assets/oneapp-home.jpg`, `assets/oneapp-remote.jpg`: Range Rover App store graphics (user supplied, already framed, no extra logo use).
- `assets/roamio-home.webp`, `assets/roamio-activity.webp`: Roamio screenshots.
- `assets/akshit-sm.jpg`: portrait, 900x1200.
- No icon set is used. All glyphs are simple shapes. Bit is drawn in code.

## Files
- `Portfolio.dc.html`: the full live site. The source of truth for layout, copy and behaviour. The logic class at the bottom holds all data arrays (WIDGETS, CAREER, ACTS, toast strings) and the `roles()` colour function.
- `Bit.dc.html`: the mascot with its poses.
- `Hero Directions.dc.html`: the review board. Turn 2 (2a to 2u) has every page, seed and state frozen, plus the motion, token, mascot and copy sheets. Turn 1 is the original exploration (ignore option 1b).
- `Screens.dc.html`: every key frame stacked at its real size (source of the screenshots).
- `screenshots/`: PNGs of each page and state. 01 to 04 are full pages (desktop 1440 and mobile 390, light and dark). 05 and 06 show the alternative seeds. 07 to 15 are states: Quick Settings, developer unlocked, spec sheet, email snackbar, career expanded, boot intro, 404 ANR. 16 shows Bit's poses. Hover, press and motion are best judged in the live files.
- `support.js`: the runtime needed to open the .dc.html files in a browser.
