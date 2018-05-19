
const child_process = require("child_process");
const util = require("util");


function open(url, type='ie'){
	let command = "";
	switch(type){
		default :
		case 'ie':
			command = "cmd /c start microsoft-edge:%s";
			break;
		case 'firefox':
			var browserExe = "C:/Program Files/Mozilla Firefox/firefox.exe";
			command = `cmd /c call \"${browserExe}\" %s`;
			break;
		case 'chrome':
			var browserExe = "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe";
			command = `cmd /c call \"${browserExe}\" %s`;
			break;
	}
	let afterCommand = util.format(command, url);
	console.log("openUrl : " + afterCommand);
	child_process.exec(afterCommand);
}

module.exports.open = open;

//----------------------------------------------------------------------
function main(){
	open("http://www.xvideos.com");
	console.log("done...");
}
// main();