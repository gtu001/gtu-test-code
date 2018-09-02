const express     = require('express');
const app = express();

//設定專案跟目錄
const {RootDirHolder} = require("E:/workstuff/workspace/gtu-test-code/GTU/node_js/system/rootDirHolder.js");
new RootDirHolder(__dirname, __filename);

//自訂package
const webLogUtil = require(rootDirHolder.path('/web/webLogUtil'));
const checkSelf = require(rootDirHolder.path("/util/checkSelf"));

//常數
const port = process.env.PORT || 3000;

