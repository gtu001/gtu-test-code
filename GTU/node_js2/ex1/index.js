var resource = [
{
	id : 1,
	name : 'Foo'
}
];

var express = require('express');
var app = express();

app.get('/', function(req, res){
	res.send('Hello World!');
});

var server = app.listen(3000, function(){
	var host = server.address().address;
	host = (host === '::' ? 'localhost' : host);
	var port = server.address().port;

	console.log("listening at http://%s:%s", host, port);
});

app.get('/resources', function(req, res){
	res.send(resources);
});

app.get('/resources/:id', function(req, res){
	var id = parseInt(req.params.id, 10);
	var result =  resources.filter(r => r.id === id)[0];

	if(!result){
		res.sendStatus(404);
	}else {
		res.send(result);
	}
});

app.post('/resources', function(req, res){
	var item = req.body;
	if(!item.id){
		return res.sendStatus(500);
	}
	resources.push(item);
	res.send('/resources/' + item.id);
});

app.put('/resources/:id', function(req,res){
	var id = parseInt(req.params.id, 10);
	var existingItem = resources.filter(r => r.id === id)[0];

	if(!existingItem){
		let item = req.body;
		item.id = id;
		resources.push(item);
		res.setHeader('Location', '/resources/' + id);
		res.sendStatus(201);
	} else {
		existingItem.name = req.body.name;
		res.sendStatus(204);
	}
});

app['delete']('/resources/:id', function(req, res){
	var id = parseint(req.params.id, 10);
	var existingItem = resources.filter(r => r.id === id)[0];
	if(!existingItem){
		return res.sendStatus(404);
	}

	resources = resources.filter(r => r.id !== id);
	res.sendStatus(204);
});

