# Portfolio revamp: "Material You playground" in Compose Multiplatform (wasmJs)

## Context
`/Users/akshit6302/Downloads/design_handoff_portfolio` is a full high-fidelity redesign of akshitnahata.com. `Portfolio.dc.html` is the source of truth: layout, copy, the data arrays and the `roles()` colour function. `README.md` maps every effect to Compose. The goal is a 100% faithful rebuild of the UI, animations and interactions in Kotlin + Compose Multiplatform for Web. All work stays on the existing **`Portfolio-revamp`** branch (already checked out). `main` is the live Netlify deploy and is only merged into when everything is done.

The current app (Voyager nav, Home/About/Project/Contact/Menu screens, Inter/Syne/Bebas fonts) gets replaced completely. The Gradle setup stays: KMP, `js` + `wasmJs`, a shared `webMain` source set, Compose 1.9.1, Kotlin 2.2.20.

## Project restructure (composeApp/src/webMain)
Delete: `screens/*`, `navigation/NavGraph.kt`, `common/*`, old `theme/*`, old drawables and fonts, and the Voyager dependency.
Add the fonts as `composeResources/font`: Bricolage Grotesque (800, plus 700 for the logo), Figtree (400/500/600/700), JetBrains Mono (500/700). These are static TTFs downloaded from Google Fonts.
Add the images to `composeResources/drawable` (copied from `assets/`): vantage-home.jpg, vantage-create.webp, oneapp-home.jpg, oneapp-remote.jpg, roamio-home.webp, roamio-activity.webp, akshit-sm.jpg.
Update `index.html`: drop the external font links, set a correct title and favicon, and add a body background so there is no flash.

New package layout under `com.akshit.portfolio`:
```
main.kt                      ComposeViewport { Portfolio() }
Portfolio.kt                 root: Box(scroll column + overlays), state hoisting
state/AppState.kt            seed, dark, whimsy, reduceMotion, qsOpen, devUnlocked, buildTaps,
                             specOpen, editMode, selectedWidget, widgetOrder, careerOpen, carActions,
                             coffee, coffeeIdx, botIdx, toast(text, action), bootSeen, screen(site/boot/404)
state/Persistence.kt         localStorage (seed, dark, whimsy, reduceMotion, akshit-boot-seen), try/catch
platform/Browser.kt          matchMedia (prefers-color-scheme, prefers-reduced-motion), clipboard,
                             openUrl(_blank), location.pathname / history.replaceState
data/Content.kt              SEEDS, BOT, COFFEE, BUBBLES, WIDGETS, CAREER, ACTS, BOOT, links (copied verbatim)
theme/Oklch.kt               oklch(L,C,h) -> sRGB Color (OKLab -> linear sRGB -> gamma, clamp)
theme/DynamicScheme.kt       roles(h, dark) exactly as in roles(), plus the extra "hue-tinted" tones used inline
                             (oklch(.2 .03 h), .17/.012, .85/.12 …) -> PortfolioColors; every role is
                             animateColorAsState(tween(400, EmphasizedEasing)); also maps onto M3 ColorScheme
theme/Type.kt                font families + the type scale
theme/Motion.kt              LocalMotion: expressive/gentle/snappy specs; whimsy off -> spring(1f,500f);
                             reduceMotion -> tween(150) fade only, with flags to disable wiggle/blink/tilt/confetti/eyes
theme/Fluid.kt               LocalContainerWidth + clampDp(min, pct, max) / clampSp (CSS clamp(…cqw…)), isCompact (<760)
ui/shapes/CssRadiusShape.kt  Shape with per-corner elliptical radii (px, % or "999"), for the blob 44/56/52/48 / 50/44/56/50,
                             the portrait 48%/48%/40/40, and every asymmetric corner; lerp-able for morphs
ui/modifiers/                ripple (custom IndicationNodeFactory: 650ms, 2.2x longest side, alpha .24->0, currentColor),
                             hoverSpring (scale/translate/rotate on hover via MutableInteractionSource),
                             focusRing (3dp primary), squash/bounce (Animatable keyframes), reveal (12% visible,
                             rise 48dp + fade, once), tilt (pointer move -> graphicsLayer rotationX/Y, spring back)
ui/layout/FlexWrap.kt        custom Layout emulating CSS flex-wrap with flex-basis + grow (used by the card rows,
                             career split, contact split)
ui/layout/DenseGrid.kt       custom Layout emulating CSS grid auto-fill minmax(150,1fr) + col/row spans + dense
                             packing (LazyVerticalGrid cannot row-span); stat tiles use the auto-fit variant
ui/bit/Bit.kt                Canvas mascot on a 120x140 base, scaled; poses idle/wave/loading/sleep/anr/dev;
                             blink every 3.4s, eye tracking (max 6dp, y*0.7) from a shared pointer position,
                             tap = jump -22dp + rotate -6° + toast
sections/StatusBar.kt        9:41, "pull" pill, Gradle build text, 4dp scroll-progress bar, app bar, nav pill/chips
sections/Hero.kt             label, name, chips, tagline, CTAs (hover scale 1.04 + radius full->22 morph), stage
                             (blob, rotated square, circle, bubble cycling every 5s, Bit 250), compact Bit badge,
                             5 stat tiles (count-up 0->36 over 1100ms ease-out cubic, coffee +1/squash/toast, apps->scroll)
sections/Work.kt             Vantage card (+ AnimatedContent spec sheet), Range Rover card (car actions morph),
                             Roamio + This portfolio, More on Play row; phones in LazyRow/snap when compact
sections/HowIBuild.kt        widgets, 480ms long press -> edit mode, ±1.4° wiggle (180–240ms alternate),
                             ring selection (4dp surface gap + 3dp primary), swap, Done button
sections/Career.kt           lifecycle timeline, node shape morph circle<->r18, animateContentSize (gentle)
sections/Contact.kt          email pill + Copy -> snackbar w/ Undo, socials, portrait + waving Bit badge
sections/Footer.kt           sleeping Bit, Build number 7-tap easter egg
overlays/QuickSettings.kt    scrim 32% + shade sliding from top (expressive), 4 tiles, 5 swatches, toasts
overlays/Toast.kt            bottom snackbar, 2.8s / 4.2s with an action, action colour oklch(.85 .13 h)
overlays/Confetti.kt         90 particles, Canvas + per-particle Animatable, colours from roles
overlays/BootIntro.kt        first visit only, lines 330ms apart from 200ms, 1.5s bar, 1.75s auto-skip, Skip, crossfade
overlays/AnrDialog.kt        404 for any path other than "/" (Netlify `/* /index.html 200` already routes them),
                             Wait -> toast, Close app -> replaceState("/") + site
```

## Key implementation notes
- **Scrolling**: one `Column(Modifier.verticalScroll(scrollState))`. Each section reports its Y via `onGloballyPositioned` into a map. Nav and CTAs call `scrollState.animateScrollTo(y - 12)` (instant when reduce motion is on). The scroll fraction (`value / maxValue`) drives the progress bar and the `:app:assembleRelease N%` text, which shows "BUILD SUCCESSFUL in 4s" at 100%.
- **Fluid sizing**: every `clamp(a, x cqw, b)` from the HTML becomes `clampDp(a, x, b)` against the root width, measured with BoxWithConstraints. Each value is copied literally from the HTML, not re-derived from the README.
- **Colour**: the hex table in the README (hue 145, light and dark) is the unit-test oracle for `Oklch.kt`.
- **Exact values come from the HTML inline styles**: paddings, gaps, font sizes, radii, shadows (`Modifier.dropShadow` with spread for the phone `0 30 60 -24` shadow), and the 1.5dp outline borders.
- **Hover-only effects** (tilt, hover scale) use pointer hover events, so touch devices are unaffected.
- **Copy rules** are checked at the end: no em or en dashes in rendered strings, and the exact strings from Content.kt.

## Build order (each step ends with a run and a visual check)
1. Clean out the old code, add fonts and assets, theme (OKLCH, roles, type, motion, fluid), shapes, ripple, and the `Portfolio` root with state.
2. Bit (all poses), checked against `screenshots/16-bit-poses.png`.
3. Status bar, app bar and Hero → 4. Work → 5. How I build → 6. Career → 7. Contact and Footer.
8. Overlays: toast, QS shade, confetti, boot intro, ANR. Then persistence and the OS media queries.
9. Motion pass: whimsy off and reduce-motion variants, reveals, focus rings and a11y labels / alt text.

## Verification
- `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`, opened in the built-in browser pane at 1440 and at 390 (mobile preset), light and dark.
- Compare section by section with `screenshots/01–04` (full pages), `05/06` (seeds), and `07–15` (QS, dev unlocked, spec sheet, snackbar, career expanded, boot, 404). Open `Portfolio.dc.html` side by side for hover and motion.
- Exercise every interaction: nav scroll, count-up replay, coffee taps and toasts, Bit taps, spec flip, car actions, long-press edit and swap, career accordion, copy then Undo, 7 build taps then confetti and dev poses, every QS toggle and swatch, boot on a cleared localStorage, `/nope` → ANR.
- Unit test (`commonTest`): OKLCH conversion matches the README hex table for primary and surface roles.
- `./gradlew :composeApp:wasmJsBrowserDistribution` builds clean. Commit on `Portfolio-revamp` only, with no push or merge to `main` unless you ask.
