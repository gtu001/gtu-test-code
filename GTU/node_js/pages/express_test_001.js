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

const courses = [
	{id : 1, name : "angular"},
	{id : 2, name : "nodeJs"},
	{id : 3, name : "reactJs"},
];

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
	const course = courses.find(c => c.id === parseInt(req.params.id));
	if (!course) {
		res.status(404).send("The course with the given ID was not found :" + req.params.id);
	}else{
		res.send(course);
	}
});

app.get("/api/posts/:year/:month", (req,res) => {
	res.send(req.params);
});

//http://127.0.0.1:3000/api/posts/test1?xxxx=eee
app.get("/api/posts/test1", (req,res) => {
	res.send(req.query);
});

app.use(express.json());//middleware
app.post("/api/courses", (req, res) => {
  const course = {
    id : courses.length + 1,
    name : req.body.name
  };
  courses.push(course);
  res.send(course);
});

app.listen(port, () => console.log(`Listening on port ${port}...`));

browserOpener.open("http://127.0.0.1:3000/", 'firefox');