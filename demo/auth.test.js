import { test } from 'node:test';
import assert from 'node:assert/strict';
import { validate, startSession, readSession, clearSession } from './auth.js';
const empty = { email: '', password: '', credentials: '' };
test('requires email and password and validates email and minimum length', () => {
  assert.equal(validate(' ', '').email, 'Enter your email.');
  assert.equal(validate('', '').password, 'Enter your password.');
  assert.equal(validate('invalid', '123').email, 'Enter a valid email address.');
  assert.equal(validate('demo@example.com', '12345').password, 'Use at least 6 characters.');
});
test('only published demo credentials work; email is trimmed and case insensitive', () => {
  assert.deepEqual(validate(' DEMO@example.com ', 'password123'), empty);
  for (const [email, password] of [['other@example.com','password123'], ['demo@example.com','password123 '], ['demo@example.com','Password123']]) {
    assert.ok(validate(email, password).credentials);
  }
});
test('session contains metadata only and logout clears it', () => {
  const map = new Map();
  const storage = { getItem: key => map.get(key), setItem: (key,value) => map.set(key,value), removeItem: key => map.delete(key) };
  assert.equal(startSession(storage, 123), true);
  assert.deepEqual(readSession(storage), { email: 'demo@example.com', startedAt: 123 });
  assert.ok(![...map.values()].join('').includes('password'));
  clearSession(storage);
  assert.equal(readSession(storage), null);
});
test('invalid or unavailable browser storage fails safely', () => {
  assert.equal(readSession(null), null);
  assert.equal(startSession(null), false);
  assert.doesNotThrow(() => clearSession(null));
  for (const value of ['{', '{}', '{"email":"other@example.com","startedAt":1}', '{"email":"demo@example.com","startedAt":0}']) {
    assert.equal(readSession({getItem: () => value}), null);
  }
});
