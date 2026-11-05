import { mkdir, copyFile } from 'node:fs/promises';
await mkdir('public', { recursive: true });
for (const file of ['index.html', 'styles.css', 'app.js', 'auth.js', 'quantum-login-demo.apk']) {
  await copyFile(`demo/${file}`, `public/${file}`);
}
