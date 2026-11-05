import { EMAIL, validate, readSession, startSession, clearSession } from './auth.js';
const $ = id => document.getElementById(id);
let storage;
try { storage = window.sessionStorage; } catch { storage = null; }
let session = readSession(storage);
const date = new Intl.DateTimeFormat('en-GB', { dateStyle: 'medium', timeStyle: 'short' });
function render() {
  $('login').hidden = !!session;
  $('profile').hidden = !session;
  if (session) {
    $('profile-email').textContent = EMAIL;
    $('started-at').textContent = date.format(session.startedAt);
    $('profile-title').focus();
  } else {
    $('form').reset();
    $('password').type = 'password';
    $('show-password').setAttribute('aria-pressed', 'false');
    $('show-password').textContent = 'Show';
  }
}
$('form').addEventListener('submit', event => {
  event.preventDefault();
  const errors = validate($('email').value, $('password').value);
  ['email', 'password'].forEach(key => {
    $(`${key}-error`).textContent = errors[key];
    $(key).setAttribute('aria-invalid', String(!!errors[key]));
  });
  $('credentials-error').textContent = errors.credentials;
  if (errors.email || errors.password) { $(errors.email ? 'email' : 'password').focus(); return; }
  if (errors.credentials) return;
  const startedAt = Date.now();
  const persisted = startSession(storage, startedAt);
  session = { email: EMAIL, startedAt };
  $('storage-note').hidden = persisted;
  $('password').value = '';
  render();
});
['email', 'password'].forEach(key => $(key).addEventListener('input', () => {
  $(`${key}-error`).textContent = '';
  $(key).removeAttribute('aria-invalid');
  $('credentials-error').textContent = '';
}));
$('show-password').addEventListener('click', () => {
  const visible = $('password').type === 'password';
  $('password').type = visible ? 'text' : 'password';
  $('show-password').textContent = visible ? 'Hide' : 'Show';
  $('show-password').setAttribute('aria-pressed', String(visible));
});
$('fill-demo').addEventListener('click', () => {
  $('email').value = EMAIL;
  $('password').value = 'password123';
  ['email', 'password'].forEach(key => { $(`${key}-error`).textContent = ''; $(key).removeAttribute('aria-invalid'); });
  $('credentials-error').textContent = '';
  $('submit').focus();
});
$('logout').addEventListener('click', () => $('logout-dialog').showModal());
$('cancel-logout').addEventListener('click', () => $('logout-dialog').close());
$('confirm-logout').addEventListener('click', () => {
  clearSession(storage);
  session = null;
  $('logout-dialog').close();
  render();
  $('email').focus();
});
render();

document.getElementById("submit").disabled = false;

