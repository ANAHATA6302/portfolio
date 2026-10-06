# Revamp progress and handoff notes

Branch: `Portfolio-revamp`. Never push or merge to `main` (it is the live Netlify deploy).
Spec: `design/handoff/README.md`, plus `design/handoff/Portfolio.dc.html` (the source of truth for exact inline values, copy and logic) and `Bit.dc.html`. Plan: `design/PLAN.md`.

## Done
- Removed the old app: Voyager, screens, nav, common, the old theme, drawables, fonts, strings and Platform stubs. `main.kt` still calls `App()`, which no longer exists. Replace it with `Portfolio()`.
- Fonts in `composeResources/font` (static TTFs from Google Fonts):
  - Bricolage: `bricolage_700_opsz24`, `bricolage_800_opsz36`, `bricolage_800_opsz96`.
  - Figtree: `figtree_400/500/600/700`.
  - JetBrains Mono: `jetbrainsmono_500/600/700`.
  - **No font has ★**. Draw the star in "★ 5.0 rating" as an inline 5-point path (InlineTextContent).
- Images in `composeResources/drawable`: `akshit.jpg` (900x1200), `oneapp_home.jpg`, `oneapp_remote.jpg` (620x1344), `vantage_home.jpg`, `vantage_create.webp`, `roamio_home.webp`, `roamio_activity.webp`.
- `theme/Oklch.kt`: oklch to sRGB with per-channel clipping. Unit test against the README hex table still to do.
- `theme/Palette.kt`: `roles(h, dark)` mirrors the JS `roles()`. `Palette` blends old and new roles over 400ms when the theme changes. `tint(l, c, dh)` gives the inline `oklch(l c var(--h)+dh)` tones, and `ink` is `oklch(.2 .03 h)`.
- `theme/Fluid.kt`: `c(min, pct, max)` is CSS clamp on cqw (the root width) and `sp(...)` is the same for font sizes. Also `compact` (< 760), `side`, `sectionTop`.
- `theme/Motion.kt`: expressive, gentle and snappy springs. Whimsy off gives spring(1, 500). Reduce motion gives tween(150). Also `playful`, `moves`, `bounceEasing`.
- `theme/Type.kt`: `rememberFonts()`, plus `Fonts.display(size, lh, tracking)` (uses the opsz96 cut at 60sp and above), `body(...)` and `mono(...)`. Line height uses em with Center/Trim.None, which matches CSS half-leading.
- `ui/CssShape.kt`: CSS border-radius as an `Outline.Rounded`, with elliptical and % corners and the CSS overflow scale-down. This matters: `999 28 28 999` on a short tile makes the 28s nearly square in the browser. Helpers: `css(all)`, `css(tl, tr, br, bl)`, `cssPct`, `Pill`, `Circle`.
- Not compiled yet. Run `./gradlew :composeApp:wasmJsBrowserDevelopmentRun` first and fix errors. Check that `Modifier.dropShadow(shape, Shadow(...))` exists in CMP 1.9.1.

## Next (in order). Exact values are in Portfolio.dc.html
1. **ui helpers**
   - Ripple: an `IndicationNodeFactory`. On a press, draw a circle at the press point, diameter 2.2x the longest side, scale 0 to 1, alpha .24 to 0, 650ms, cubic(.2,0,0,1), colour = the content colour, drawn over the content and clipped to the shape. Only elements with `onPointerDown=ripple` get it.
   - Tap modifier: hand cursor, a 3dp primary focus ring, and a shared `MutableInteractionSource` so hover can drive effects.
   - Every `<a>` has `hover: opacity .85`.
   - **FlexWrap** Layout that emulates CSS flex-wrap: basis, grow, shrink, min and max width, auto margins (`margin-left: auto`, `margin: 0 auto`), justify space-between, align center, end or stretch. Stretch needs intrinsics, so avoid Lazy layouts inside it.
   - **Grid** Layout: auto-fit or auto-fill `minmax(min(100%, N), 1fr)` with col/row spans and dense packing. For widgets, cols = floor((W+gap)/(150+gap)) and row height clamp(150, 12cqw, 176). For auto-fit, empty tracks collapse.
   - `reveal()`: only for items below the fold at load. Starts at opacity 0 and +48dp, and animates once when 12% is visible (opacity 500ms, translate gentle). Not used under reduce motion.
   - `bounce()`: WAAPI-exact keyframes, with the easing applied to overall progress and the last segment extrapolated on overshoot. Jump is 0 → (-22dp, -6°) → 0 over 650ms. Squash is scale 1 → .93 → 1.05 → 1 over 480ms.
   - Tilt: rotateY = px*10, rotateX = -py*8 on the inner phone row. cameraDistance is about perspective 1200. Springs back on exit.
2. **Bit** on Canvas, base 120x140 scaled by size/120. Geometry derived from Bit.dc.html (base units):
   - Frame and body:
     - Antenna stick: (58, 6) 4x20 r2.
     - Ball: (51, 0) 18 circle, tertiary.
     - Arms: (0, 56) and (106, 56), 14x34 r7. Wave rotates the right arm -150° about (113, 63).
     - Feet: (28, 112) and (70, 112), 22x20, radius 6 6 10 10.
     - Body: (10, 22) 100x96 r36, primary. The inner bottom shade is the body minus the body shifted up 8, filled black at .14.
     - Screen: (22, 36) 76x50 r20, ink.
   - Faces (glyph colour is tint(.9, .14)):
     - Normal eyes: 11x18 r6 at x 40 and 69, y 46.5. Smile: 14x6 at (53, 69.5), bottom radius about 6.
     - Sleep eyes: 14x4 r2 at x 38 and 68, y 53.5, with a smile.
     - Loading: three 8px dots at x 41, 56 and 71, y 53, opacity 1, .6 and .3. Flat mouth 14x3 at (53, 71).
     - ANR: two 14px rings with a 3px stroke at x 38 and 68, y 50, plus the flat mouth.
   - Extras:
     - Dev shades: #111 lenses 34x20, radius 6 6 12 12, at (18, 45) and (68, 45). Bridge (52, 53) 16x4.
     - Dev hat: a triangle with apex (60, -22) and base y 18 from x 42 to 78, tertiary. Pom: (54, -30) 12 circle, primaryContainer.
     - Cheeks: (26, 96) and (82, 96), 12x6 r3, tertiaryContainer.
     - Zzz: mono 700 18 "z" at (100, -4), then a 13px "z" raised 10, onSurfaceVariant.
     - Sweat: (100, 20) 12x12, radius 50% 0 50% 50%, rotated -45°, tertiary.
   - Behaviour:
     - Global blink every 3.4s: eyes scaleY .1 for 130ms.
     - Eyes follow the pointer: translate (dx/d*m, dy/d*m*.7) with m = min(6, d/40), in base units. Use a root pointer tracker.
     - A tap anywhere on Bit triggers the jump and a toast. In dev mode the toast is "Nice shades, right? Developer perks.".
3. **Sections**: StatusBar, Hero, Work, HowIBuild, Career, Contact, Footer.
4. **Overlays**: Toast, QuickSettings, Confetti (z under QS), BootIntro, AnrDialog (shown when pathname != "/").
5. **State**: persist seed, dark, whimsy and reduceMotion, plus `akshit-boot-seen`, in localStorage. Default dark and reduce-motion from matchMedia.
6. Update `index.html`: remove the old font links, set the title "Akshit Nahata", and set the body bg.
7. **Verify** in a browser at 1440 and 390, light and dark, against `design/handoff/screenshots`.

## Gotchas found
- Section `max-width: 1328` is content-box. Padding adds on top, so the outer width reaches 1440.
- The nav pill selection is static: "Work" is always selected.
- The status bar and app bar scroll with the page. They are not sticky.
- Hero CTAs are `flex: 1 1 auto; max-width: 260`. On mobile they wrap onto 2 lines.
- After a widget long press, releasing still fires click, which selects that widget. That matches the reference.
- The wiggle duration is 180 + (i%3)*30 ms, alternating ±1.4°, starting at -1.4.
- Bubble text cycles every 5s with no animation.
- Status text is `:app:assembleRelease N%`, or `N%` on compact, and "BUILD SUCCESSFUL in 4s" at 100%.
