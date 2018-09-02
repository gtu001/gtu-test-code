const { spawn, spawnSync } = require('child_process');
const ls = spawn('ls', ['-lh', '/usr']); //--非同步
const ls2 = spawnSync('ls', ['-lh', '/usr']) //--同步

ls.stdout.on('data', (data) => {
  console.log(`stdout: ${data}`);
});

ls.stderr.on('data', (data) => {
  console.log(`stderr: ${data}`);
});

ls.on('close', (code) => {
  console.log(`child process exited with code ${code}`);
});
