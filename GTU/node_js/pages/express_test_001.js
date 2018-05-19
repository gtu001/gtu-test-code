const express     = require('express');
const app = express();

//設定專案跟目錄
const {RootDirHolder} = require("E:/workstuff/workspace/gtu-test-code/GTU/node_js/system/rootDirHolder.js");
new RootDirHolder();

//自訂package
const webLogUtil = require(rootDirHolder.path('/web/webLogUtil'));
const checkSelf = require(rootDirHolder.path("/util/checkSelf"));
const browserOpener = require(rootDirHolder.path("/web/browserOpener"));


//常數
const port = process.env.PORT || 3000;


app.get("/", (req, res) => {
	appender = new webLogUtil.ResponseAppender(res);    
    appender.print("Hello World!");
    appender.print("測試");
    appender.flush();
});

app.get("/api/courses", (req, res) => {
    res.send("[1,2,3,4]");
});

app.get("/api/courses/:id", (req,res) => {
	var id = req.params.id;
	res.send(`send id : ${id}`);
});

app.get("/api/posts/:year/:month", (req,res) => {
	res.send(req.params);
});

//http://127.0.0.1:3000/api/posts/test1?xxxx=eee
app.get("/api/posts/test1", (req,res) => {
	res.send(req.query);
});


app.listen(port, () => console.log(`Listening on port ${port}...`));

browserOpener.open("http://127.0.0.1:3000/", 'firefox');