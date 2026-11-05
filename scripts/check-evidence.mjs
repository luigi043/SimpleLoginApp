import { readFile, access, readdir } from 'node:fs/promises';
import path from 'node:path';
for (const file of ['README.md', 'docs/verification/SCREEN-INDEX.md', 'docs/verification/RESULTS.md', 'docs/verification/CHANGES.md']) {
  const text = await readFile(file, 'utf8');
  for (const match of text.matchAll(/\]\(([^)]+)\)/g)) {
    if (/^https?:|^#/.test(match[1])) continue;
    await access(path.resolve(path.dirname(file), match[1].split('#')[0]));
  }
}
const native = await readdir('docs/verification/screenshots/native');
const web = (await readdir('docs/verification/screenshots')).filter(file => file.endsWith('.png'));
if (native.length !== 10 || web.length !== 14) throw Error(`Unexpected screenshot inventory: ${native.length} native, ${web.length} browser`);
console.log('Documentation links exist; 10 native and 14 browser captures verified.');
