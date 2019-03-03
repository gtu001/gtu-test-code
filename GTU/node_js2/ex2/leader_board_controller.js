'use strict'

var BoardsService = require('./leader_board_service');

class BoardsController {
	constructor(router){
		this.router = router;
		this.registerRoutes();
	}

	//http://localhost:3000/api/v1/board
	registerRoutes(){
		this.router.get('/board', this.getBoards.bind(this));
		this.router.get('/board/:id', this.getSingleBoard.bind(this));
		this.router.post('/board', this.postBoard.bind(this));
		this.router.put('/board/:id', this.putBoard.bind(this));
	}

	getBoards(req, res){
		var board = BoardsService.getBoards();
		res.send(board);
	}

	getSingleBoard(req, res){
		var id = req.params.id;
		var board = BoardsService.getSingleBoard(id);
		if(!board){
			res.sendStatus(404);
		} else {
			res.send(board);
		}
	}

	putBoard(req, res){
		var id = parseInt(req.params.id, 10);
		var existingBoard = BoardsService.getSinglePlayer(id);

		if(!existingBoard){
			let board = BoardsService.addBoard(req.body.boardName, req.body.rankDirection);
			if(board){
				res.setHeader('Location', '/boards/' + id);
				res.sendStatus(201);
			} else {
				res.sendStatus(500);
			}
		} else {
			if(BoardsService.updateBoard(id, req.body)){
				res.sendStatus(204);
			}else {
				res.sendStatus(404);
			}
		}
	}

	postBoard(req, res){
		let board = BoardsService.addBoard(req.body.boardName, req.body.rankDirection);
		if(board){
			res.setHeader('Location', '/board/' + boardInfo.id);
			res.sendStatus(200);
		}else {
			res.sendStatus(500);
		}
	}
}

module.exports = BoardsController;