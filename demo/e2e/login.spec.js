import { test, expect } from '@playwright/test';
import { mkdir, readFile } from 'node:fs/promises';
import { createHash } from 'node:crypto';

test('validation, login, refresh, cancel and confirm logout', async ({ page }, testInfo) => {
  await mkdir('docs/verification/screenshots', { recursive: true });
  await page.goto('/');
  await expect(page.getByRole('button', { name: 'Log in to demo' })).toBeEnabled();
  await expect(page.locator('body')).toHaveJSProperty('scrollWidth', await page.locator('body').evaluate(el => el.clientWidth));
  await page.getByRole('button', { name: 'Log in to demo' }).click();
  await expect(page.locator('#email-error')).toHaveText('Enter your email.');
  await expect(page.locator('#password-error')).toHaveText('Enter your password.');
  await page.screenshot({ path: `docs/verification/screenshots/web-${testInfo.project.name}-required-fields.png`, fullPage: true });
  await page.getByLabel('Email', { exact: true }).fill('invalid');
  await page.getByLabel('Password', { exact: true }).fill('123');
  await page.getByRole('button', { name: 'Log in to demo' }).click();
  await expect(page.locator('#email-error')).toHaveText('Enter a valid email address.');
  await expect(page.locator('#password-error')).toHaveText('Use at least 6 characters.');
  await page.screenshot({ path: `docs/verification/screenshots/web-${testInfo.project.name}-invalid-input.png`, fullPage: true });
  await page.getByLabel('Email', { exact: true }).fill('other@example.com');
  await page.getByLabel('Password', { exact: true }).fill('wrong-password');
  await page.getByRole('button', { name: 'Log in to demo' }).click();
  await expect(page.getByRole('alert')).toContainText('Credentials do not match');
  await page.screenshot({ path: `docs/verification/screenshots/web-${testInfo.project.name}-credentials-mismatch.png`, fullPage: true });
  await page.getByRole('button', { name: 'Fill sample credentials' }).click();
  await page.getByRole('button', { name: 'Show', exact: true }).click();
  await expect(page.locator('#password')).toHaveAttribute('type', 'text');
  await page.getByRole('button', { name: 'Hide', exact: true }).click();
  await expect(page.locator('#password')).toHaveAttribute('type', 'password');
  await mkdir('docs/verification/screenshots', { recursive: true });
  await page.screenshot({ path: `docs/verification/screenshots/web-${testInfo.project.name}-login.png`, fullPage: true });
  await page.getByRole('button', { name: 'Log in to demo' }).click();
  await expect(page.getByRole('heading', { name: 'Your demo profile' })).toBeVisible();
  await page.reload();
  await expect(page.getByRole('heading', { name: 'Your demo profile' })).toBeVisible();
  expect(await page.evaluate(() => JSON.stringify(sessionStorage))).not.toContain('password');
  await page.screenshot({ path: `docs/verification/screenshots/web-${testInfo.project.name}-profile.png`, fullPage: true });
  await page.getByRole('button', { name: 'Log out', exact: true }).click();
  await expect(page.getByRole('dialog')).toBeVisible();
  await page.screenshot({ path: `docs/verification/screenshots/web-${testInfo.project.name}-logout-dialog.png`, fullPage: true });
  await page.getByRole('button', { name: 'Cancel' }).click();
  await expect(page.getByRole('dialog')).not.toBeVisible();
  await page.getByRole('button', { name: 'Log out', exact: true }).click();
  await page.getByRole('dialog').getByRole('button', { name: 'Log out', exact: true }).click();
  await expect(page.getByRole('button', { name: 'Log in to demo' })).toBeVisible();
  await page.reload();
  await expect(page.getByRole('button', { name: 'Log in to demo' })).toBeVisible();
});

test('keyboard login and Escape dismiss logout without losing session', async ({ page }) => {
  await page.goto('/');
  await page.getByLabel('Email', { exact: true }).fill(' DEMO@example.com ');
  await page.getByLabel('Password', { exact: true }).fill('password123');
  await page.getByLabel('Password', { exact: true }).press('Enter');
  await expect(page.getByRole('heading', { name: 'Your demo profile' })).toBeVisible();
  await page.getByRole('button', { name: 'Log out', exact: true }).click();
  await page.keyboard.press('Escape');
  await expect(page.getByRole('dialog')).not.toBeVisible();
  await expect(page.getByRole('heading', { name: 'Your demo profile' })).toBeVisible();
});

test('JavaScript disabled cannot submit credentials', async ({ browser }) => {
  const context = await browser.newContext({ javaScriptEnabled: false });
  const page = await context.newPage();
  await page.goto(process.env.DEMO_URL || 'http://127.0.0.1:4173');
  await expect(page.getByRole('button', { name: 'Log in to demo' })).toBeDisabled();
  await expect(page.locator('#password')).not.toHaveAttribute('name');
  await context.close();
});

test('blocked tab storage falls back to memory and shows a notice', async ({ page }, testInfo) => {
  await page.addInitScript(() => Object.defineProperty(window, 'sessionStorage', { get() { throw new DOMException('Blocked', 'SecurityError'); } }));
  await page.goto('/');
  await page.getByRole('button', { name: 'Fill sample credentials' }).click();
  await page.getByRole('button', { name: 'Log in to demo' }).click();
  await expect(page.locator('#storage-note')).toBeVisible();
  await page.screenshot({ path: `docs/verification/screenshots/web-${testInfo.project.name}-storage-unavailable.png`, fullPage: true });
  await page.reload();
  await expect(page.getByRole('button', { name: 'Log in to demo' })).toBeVisible();
});

test('public deployment serves security headers and the exact tested APK', async ({ request }) => {
  test.skip(!process.env.DEMO_URL, 'Publication check runs against the public deployment.');
  const home = await request.get('/');
  expect(home.status()).toBe(200);
  expect(home.headers()['content-security-policy']).toContain("connect-src 'none'");
  expect(home.headers()['x-content-type-options']).toBe('nosniff');
  const apk = await request.get('/quantum-login-demo.apk');
  expect(apk.status()).toBe(200);
  const digest = data => createHash('sha256').update(data).digest('hex');
  expect(digest(await apk.body())).toBe(digest(await readFile('demo/quantum-login-demo.apk')));
});
