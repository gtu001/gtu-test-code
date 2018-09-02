

class ResponseAppender {

	constructor(res){
		this.res = res;
		this.fullMessage = "";
	}

	print(message){
		console.log(message);
		this.fullMessage += message + "<br/>";
	}

	flush(){
		this.res.send(this.fullMessage);
	}
}


module.exports.ResponseAppender = ResponseAppender;
