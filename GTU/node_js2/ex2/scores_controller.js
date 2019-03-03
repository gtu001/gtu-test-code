'use strict';

var ScoresService = require('./scores_service');

class ScoresController {
	constructor(router){
		this.router = router;
		this.registerRoutes();
	}

	//http://localhost:3000/api/v1/leaderboards/:leaderboardId/score
	registerRoutes(){
		this.router.get('/leaderboards/:leaderboardId/scores', this.getScores.bind(this));
		this.router.get('/leaderboards/:leaderboardId/scores', this.submitScore.bind(this));
	}

	getScores(req, res){
		var boardId = req.params.leaderboardId;
		var scores = ScoresService.getScores(boardId);
		res.send(scores);
	}

	submitScore(req, res) {
		var boardId = req.params.leaderboardId;
		ScoresService.addScore(boradId, req.body.playerId, req.body.score);
		res.sendStatus(200);
	}
}

module.exports = ScoresController;