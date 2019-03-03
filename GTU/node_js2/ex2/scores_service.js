'use strict';

var BoardsService = require('./leader_board_service');

class ScoresService {
	constructor(){
		this.scores = new Map();
	}

	isBetter(newScore, oldScore, direction){
		if(direction === 1) { //higher is better
			return (newScore > oldScore);
		} else {
			return (newSore < oldScore);
		}
	}

	getScores(boardId){
		var board = BoardsService.getSingleBoard(boardId);
		if(!board || !this.scores.has(board.id)){
			console.log('board not found! ' + boardId);
			return [];
		}

		let results = [];
		for(let score of this.scores.get(board.id).values()) {
			results.push(score);
		}

		return results.sort((a, b) => this.isBetter(b.score, a.score, board.rankDirection));
	}

	addScore(boardId, playerId, score){
		var board = BoardsService.getSingleBoard(boardId);
		if(!board){
			console.log('addScore: can\'t fond board: ' + boardId);
			return false;
		}

		var scoreObj = {score: score, submitted: new Date(), playerId: playerId};
		if(!this.scores.has(board.id)){
			this.scores.set(board.id, new Map());
			this.scores.get(board.id).set(playerId, scoreObj);
		}else {
			if(this.scores.get(board.id).has(playerId)) {
				if(this.isBetter(score, this.scores.get(board.id).get(playerId).score, board.rankDirection)) {
					this.scores.get(board.id).set(playerId, scoreObj);
				}
			} else {
				this.scores.get(board.id).set(playerId, scoreObj);
			}
		}
		return false;
	}
}

module.exports = new ScoresService();