
const os = require("os");
const process = require("process");
const child_process = require("child_process");
const stringUtil = require("./stringUtil");

function showMembers(libVar){
	console.log("Start -----------------------------------------------");
	for(let i in libVar){
		var typeStr = typeof(libVar[i]);
		console.log(`${i}\t\t${typeStr}`);
	}
	console.log("End  -----------------------------------------------");
}

function nodeDocument(nodeLib){
	if(stringUtil.isBlank(nodeLib)){
		console.log(`nodeLib is empty : ${nodeLib}`)
		return;
	}
	child_process.exec(`cmd /c start https://nodejs.org/api/${nodeLib}.html`);
}


module.exports.showMembers = showMembers;
module.exports.nodeDocument = nodeDocument;
