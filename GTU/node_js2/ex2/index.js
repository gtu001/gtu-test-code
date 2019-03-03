var express = require('express');
var app = express();

app.get('/', function(req, res){
	res.send('Hello World!');
});

//----------------------------------------------------------------------------------------------------------------------------

//Swagger Docs
var swaggerTools = require('swagger-tools');
//swaggerRouter configuration
var options = {
	swaggerUi: '/swagger.json',
	controllers: './'
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
})

//----------------------------------------------------------------------------------------------------------------------------


//start the server
var server = app.listen(3000, function(){
	var host = server.address().address;
	host = (host === '::' ? 'localhost' : host);
	var port = server.address().port;
	console.log('listening at http://%s:%s', host, port);
});



var apiRouter = express.Router();
app.use('/api', apiRouter);

var apiV1 = express.Router();
apiRouter.use('/v1', apiV1);

var playersApiV1 = express.Router();
apiV1.use('/players', playersApiV1);

var boardsApiV1 = express.Router();
apiV1.use('/leaderboards', boardsApiV1);


//------------------------------------------------------
var PlayerController = require('./players_controller');
var pc = new PlayerController(apiV1);


//------------------------------------------------------
var BoardController = require('./leader_board_controller');
var bc = new BoardController(apiV1);

var BoardService = require('./leader_board_service');
BoardService.addBoard('Total Score', 1);
BoardService.addBoard('Times Died', 2);


//------------------------------------------------------

var ScoresController = require('./scores_controller');
var sc = new ScoresController(apiV1);

var ScoresService = require('./scores_service');