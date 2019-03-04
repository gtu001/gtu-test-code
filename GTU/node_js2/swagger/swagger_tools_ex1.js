//Swagger Docs
var swaggerTools = require('swagger-tools');
//swaggerRouter configuration
var options = {
	swaggerUi: '/swagger.json',
	controllers: './controllers'
};

//The Swagger document (require it, build it programmatically, fetch it from a URL, ...)
var swaggerDoc = require('./swagger.json');

//Initialize the Swagger middleware
swaggerTools.initializeMiddleware(swaggerDoc, function(middleware){
	//Interpret Swagger resources and attach metadata to request - must be first in swagger-tools middleware chain
	app.use(middleware.swaggerMetadata());

	//Validate Swagger requests
	app.use(middleware.swaggerValidator());

	//Route validated requests to appropriate controller
	app.use(middleware.swaggerRouter(options));

	//Serve the Swagger documents and Swagger UI
	app.use(middleware.swaggerUi());

	//start the server
	var server = app.listen(3000, function(){
		var host = server.address().address;
		host = (host === '::' ? 'localhost' : host);
		var port = server.address().port;
		console.log('listening at http://%s:%s', host, port);
	});
})