

function isBlank(strVar){
	if(strVar == undefined){
		console.log(`undefined = ${strVar}`);
		return true;
	}
	strVar = String(strVar);
	if (strVar == null || strVar.trim().length == 0){
		console.log(`isBlank = ${strVar}`);
		return true;
	}
	return false;
}

module.exports.isBlank = isBlank;