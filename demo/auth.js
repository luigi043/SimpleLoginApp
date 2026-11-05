export const EMAIL = 'demo@example.com';
export const PASSWORD = 'password123';
export function validate(email, password) {
  const normalized = email.trim();
  const errors = {
    email: !normalized ? 'Enter your email.' : !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(normalized) ? 'Enter a valid email address.' : '',
    password: !password ? 'Enter your password.' : password.length < 6 ? 'Use at least 6 characters.' : '',
    credentials: ''
  };
  if (!errors.email && !errors.password && (normalized.toLowerCase() !== EMAIL || password !== PASSWORD)) {
    errors.credentials = 'Credentials do not match. Use the sample email and password below.';
  }
  return errors;
}
export function readSession(storage) {
  try {
    const value = JSON.parse(storage.getItem('quantum-demo-session'));
    return value?.email === EMAIL && Number.isFinite(value?.startedAt) && value.startedAt > 0 ? value : null;
  } catch { return null; }
}
export function startSession(storage, startedAt = Date.now()) {
  const session = { email: EMAIL, startedAt };
  try { storage.setItem('quantum-demo-session', JSON.stringify(session)); return true; } catch { return false; }
}
export function clearSession(storage) {
  try { storage.removeItem('quantum-demo-session'); } catch { /* The in-memory view still signs out. */ }
}
