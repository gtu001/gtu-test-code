

class WebLogUtil {

	private fullMessage : string;

	constructor(private res : any){
	}

	log(message){
		console.log(message);
		fullMessage += message + "<br/>";
	}

	flush(){
		res.send(fullMessage);
	}
}


module.exports.webLogUtil = WebLogUtil;