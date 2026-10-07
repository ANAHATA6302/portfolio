# Revamp progress and handoff notes

Pushing to `main` deploys the live site (GitHub Actions to Netlify). Do new work on a branch and merge when it's checked.
Spec: `design/handoff/README.md`, plus `design/handoff/Portfolio.dc.html` (the source of truth for exact inline values, copy and logic) and `Bit.dc.html`.

## Status: feature complete, verified against the screenshots
Everything in the plan is built and checked in a real browser at 1440 and 390, light and dark, against `design/handoff/screenshots`:
- Status bar, app bar, nav, Hero, Work (Vantage + spec sheet, Range Rover + car actions, Level Shoes client card with quote, Roamio, This portfolio, More on Play), How I build (long press, wiggle, select, swap, Done), Career accordion (original write-ups from the old site, no RESUMED badge), Contact (copy + Undo snackbar), Footer, Build number in Quick Settings (7 taps: toast, confetti, dev Bit everywhere, QS tile Unlocked).
- Overlays: toast/snackbar, Quick Settings (all tiles and swatches, scrim closes it), confetti, boot intro (first visit, Skip), 404 ANR (any path but `/`).
- Seeds re-theme everything (checked violet light and tangerine dark). State persists in localStorage.
- `webTest/OklchTest.kt` checks the OKLCH conversion against the README hex tables (runs in CI).

## Building
- `./gradlew :composeApp:wasmJsBrowserDevelopmentRun` (or `wasmJsBrowserDistribution`). The `js` target also compiles.
- CI: `.github/workflows/build-main.yml` runs the unit tests, builds the wasm bundle and deploys it to Netlify on every push to `main`. A failing test stops the deploy.
- Build outputs, Gradle/Kotlin caches, `.idea` and `local.properties` are git-ignored.
- Cloud sessions need `dl.google.com` allowed in the environment's network settings (Compose 1.9 pulls androidx from Google Maven). If Maven Central rate-limits (HTTP 429), a local `~/.gradle/init.d` script that puts `https://maven-central.storage-download.googleapis.com/maven2` first fixes it.

## Code map (composeApp/src/webMain/kotlin/com/akshit/portfolio)
- `Portfolio.kt` root: palette, fluid width, motion, pointer tracking, scroll column, overlays.
- `state/AppState.kt` every piece of state and handler from the reference `Component` class. `data/Content.kt` copy. `platform/Browser.kt` localStorage, matchMedia, clipboard, open, pathname.
- `theme/` OKLCH roles (animated 400ms), fluid clamp, motion tokens, fonts.
- `ui/` CssShape, ripple + `tap()` (focus ring only after Tab), FlexWrap (CSS flex-wrap incl. basis/grow/shrink/min-content/auto margins/stretch), CssGrid (auto-fit/auto-fill, spans, dense), reveal, bounce (WAAPI keyframes), tilt, phones, `T()` text.
- `bit/Bit.kt` the mascot on Canvas. `sections/` and `overlays/` one file per area.

## Gotchas found
- Compose web sizes text with line-height under the natural height from the first line's natural top to the last line's natural bottom. `T()` trims the excess like CSS half-leading. Always use `T()` for text.
- CSS never breaks inside a word; Compose does. `T()` measures at least the longest word wide and lets it overflow.
- Content-box widths: section max 1328 and the ANR dialog 440 exclude padding.
- The nav pill selection is static: "Work" is always selected. Status bar and app bar scroll with the page.
- Build number lives in Quick Settings (under the tiles), not the footer, with a "Tap 7 times" hint that counts down.
- After a widget long press, releasing still fires click, which selects that widget (matches the reference).
- Wiggle duration is 180 + (i%3)*30 ms, alternating ±1.4°, starting at -1.4. It stops with whimsy off or reduce motion (README rule, over the prototype), and so does confetti.
- Headless Chrome needs an explicit locale or Compose throws "Incorrect locale information provided" at startup.
- Phone rows on narrow screens start at the first phone and scroll, instead of the reference's centred-overflow clipping (agreed: keep the better behaviour).

## Left to consider
- Optional polish: per-frame motion comparison against `Portfolio.dc.html` (springs are token-mapped, not keyframe-identical).
