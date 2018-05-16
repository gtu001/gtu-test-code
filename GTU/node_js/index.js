const express     = require('express');
const webLogUtil = require('/web/webLogUtil');


const app = express();
const port = process.env.PORT || 3000;



app.get("/", (req, res) => {
    res.send("Hello World");
});

app.get("/api/courses", (req, res) => {
    res.send("[1,2,3,4]");
});

app.listen(port, () => console.log(`Listening on port ${port}...`));