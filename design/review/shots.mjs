// Captures review screenshots of the built site. Run from CI: node design/review/shots.mjs <baseUrl> <outDir>
import { chromium } from 'playwright';
import fs from 'node:fs';

const [base, out] = process.argv.slice(2);
fs.mkdirSync(out, { recursive: true });
const configs = [
  { name: 'desk-light', w: 1440, h: 900, dark: false },
  { name: 'desk-dark', w: 1440, h: 900, dark: true },
  { name: 'mob-light', w: 390, h: 844, dark: false },
  { name: 'mob-dark', w: 390, h: 844, dark: true },
];
const log = [];
const browser = await chromium.launch();

async function open(c, { boot = false, path = '/' } = {}) {
  const page = await browser.newPage({ viewport: { width: c.w, height: c.h }, colorScheme: c.dark ? 'dark' : 'light' });
  page.on('pageerror', e => log.push(`${c.name} pageerror: ${e.message}`));
  page.on('console', m => { if (m.type() === 'error') log.push(`${c.name} console: ${m.text()}`); });
  if (!boot) await page.addInitScript(() => localStorage.setItem('akshit-boot-seen', '1'));
  await page.goto(base + path);
  await page.waitForTimeout(boot ? 900 : 5000);
  return page;
}
const shot = (page, n) => page.screenshot({ path: `${out}/${n}.jpg`, type: 'jpeg', quality: 70 });

for (const c of configs) {
  const page = await open(c);
  await page.mouse.move(c.w / 2, c.h / 2);
  for (let i = 0; i < 16; i++) {
    await shot(page, `${c.name}-${String(i).padStart(2, '0')}`);
    const before = await page.screenshot();
    await page.mouse.wheel(0, c.h - 60);
    await page.waitForTimeout(1600);
    const after = await page.screenshot();
    if (Buffer.compare(before, after) === 0) break;
  }
  await page.close();
}

// States, desktop light unless noted
const d = configs[0], m = configs[3];
{
  const p = await open(d);
  await p.mouse.click(1357, 84); // QS button
  await p.waitForTimeout(1200);
  await shot(p, 'state-qs');
  await p.close();
}
{
  const p = await open(d, { boot: true });
  await shot(p, 'state-boot');
  await p.close();
}
{
  const p = await open(d, { path: '/nope' });
  await shot(p, 'state-404');
  await p.close();
}
{
  const p = await open(m, { path: '/nope' });
  await shot(p, 'state-404-mob-dark');
  await p.close();
}
fs.writeFileSync(`${out}/log.txt`, log.join('\n') + '\n');
await browser.close();
