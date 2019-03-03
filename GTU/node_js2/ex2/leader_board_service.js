'use strict';

var uuid = require('node-uuid');

class LeaderBoardService {
	constructor(){
		this.boards = []
	}

	getBoards(){
		return this.boards;
	}

	getSingleBoard(boardId){
		var board = this.boards.filter(p => p.id === boardId)[0];
		return board || null;
	}

	addBoard(boardName, rankDirection){
		var existingBoard = this.boards.filter(p => (p.boardName === boardName)).length > 0;
		if(!existingBoard){
			return null;	
		}

		var board = {boardName : boardName, rankDirection: rankDirection};
		board.id = uuid.v4();
		this.boards.push(board);
		return board;
	}

	updateBoard(boardId, info){
		var board = this.getSingleBoard(boardId);
		if(board){
			board.boardName = info.boardName ? info.boardName : board.boardName;
			board.rankDirection = info.rankDirection ? info.rankDirection : board.rankDirection;
			return true;
		}
		return false;
	}
}

module.exports = new LeaderBoardService();