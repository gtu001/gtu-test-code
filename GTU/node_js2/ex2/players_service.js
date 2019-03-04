'use strict';

var uuid = require('node-uuid');

class PlayersService {
	constructor(){
		this.players = [];
	}

	getPlayers(){
		return this.players;
	}

	getSinglePlayer(playerId){
		var player = this.players.filter(p => p.id === playerId)[0];
		return player || null;
	}

	addPlayer(info){
		//prevent a bit of bad/duplicate data
		if(!info || this.players.filter(p => (p.firstName === info.firstName && p.lastName === info.lastName)).length > 0){
			return false;	
		}
		info.id = uuid.v4();
		this.players.push(info);
		return true;
	}

	updatePlayer(playerId, info){
		var player = this.getSinglePlayer(playerId);
		if(player){
			player.firstName = info.firstName ? info.firstName : player.firstName;
			player.lastName = info.lastName ? info.lastName : player.lastName;
			player.displayName = info.displayName ? info.displayName : player.displayName;
			return true;
		}
		return false;
	}
}

module.exports = new PlayersService();