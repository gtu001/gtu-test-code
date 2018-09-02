const path = require('path');
const process =  require("process"); 


console.log("appDir = " + path.dirname(require.main.filename));

console.log("cwd = "+process.cwd());