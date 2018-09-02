	
	// check is radio have checked
	function isRadioChecked(array, object, message){
		var radioNum = 0;
		for(i=0; i<array.length; i++){
			if(array(i).checked == true){
				break;
			}
			radioNum++;
		}
		
		if(radioNum == array.length){
			alert(message);
			object.focus();
			return false;
		}
		
		return true;
	}
	
	// check email is valid
	function isValidEmail(obj, message){
		if(obj.value.charAt(0) == "." ||
		   obj.value.charAt(0) == "@" ||
		   obj.value.indexOf('@', 0) == -1 ||
		   obj.value.indexOf('.', 0) == -1 ||
		   obj.value.lastIndexOf("@") == obj.value.length -1 ||
		   obj.value.lastIndexOf(".") == obj.value.length -1){
	   	   
	   	   	alert(message);
			obj.focus();
			return false;
		}
		return true;
	}
	
	// check is var a num
	function checkIsNum(num) {
		var i,j,strTemp;
		strTemp="0123456789";
		if (num.length == 0){
			return false;
		}
		
		for (i=0;i< num.length; i++){
			j=strTemp.indexOf(num.charAt(i));
			if (j==-1) {
				return false;
			}
		}
		return true;
	}
	
	var ALP_STR = "ABCDEFGHJKLMNPQRSTUVXYWZIO";
	var NUM_STR = "0123456789";

	function trim(str) {
		while (str.indexOf(" ")==0) {
			str = str.substring(1, str.length);
		}
		while ((str.length>0) && (str.lastIndexOf(" ")==(str.length-1))) {
			str = str.substring(0, str.length-1);
		}
		return str;
	}
	
	// check idn character is valid
	function chkPID_CHAR(sPID) {
		var sMsg = "";
		var iPIDLen = String(sPID).length;
	
		var sChk = ALP_STR + NUM_STR;
		for(i=0;i<iPIDLen;i++) {
			if (sChk.indexOf(sPID.substr(i,1)) < 0) {
				sMsg = "\u9019\u500b\u8eab\u5206\u8b49\u5b57\u865f\u542b\u6709\u4e0d\u6b63\u78ba\u7684\u5b57\u5143\uff01";
				break;
			}
		}
	
		if (sMsg.length == 0) {
			if (ALP_STR.indexOf(sPID.substr(0,1)) < 0) {
				sMsg = "\u8eab\u5206\u8b49\u5b57\u865f\u7b2c 1 \u78bc\u61c9\u70ba\u82f1\u6587\u5b57\u6bcd(A~Z)\u3002";
			} else if ((sPID.substr(1,1) != "1") && (sPID.substr(1,1) != "2")) {
				sMsg = "\u8eab\u5206\u8b49\u5b57\u865f\u7b2c 2 \u78bc\u61c9\u70ba\u6578\u5b57(1~2)\u3002";
			} else {
				for(var i=2; i<iPIDLen; i++) {
					if (NUM_STR.indexOf(sPID.substr(i, 1)) < 0) {
						sMsg = "\u7b2c " + (i+1) + " \u78bc\u61c9\u70ba\u6578\u5b57(0~9)\u3002";
						break;
					}
				}
			}
		}
	
		if (sMsg.length != 0) {
			alert(sMsg);
			return false;
		} else {
			return true;
		}
	}
	
	function getPID_SUM(sPID) {
		var iChkNum = 0;
	
		// 1 char
		iChkNum = ALP_STR.indexOf(sPID.substr(0,1)) + 10;
		iChkNum = Math.floor(iChkNum/10) + (iChkNum%10*9);
		
		
		// 2 - 9 char
		for(var i=1; i<sPID.length-1; i++) {
			iChkNum += sPID.substr(i,1) * (9-i);
		}
	
		// 10 char
		iChkNum += sPID.substr(9,1)*1;
	
		return iChkNum;
	}

	// check PID --  input the id value for args
	function CheckPID(sPID){
		if(sPID == ''){
			alert("\u8acb\u8f38\u5165\u8eab\u5206\u8b49\u5b57\u865f");
			return false;
		} else if(sPID.length != 10){
			alert("\u8eab\u4efd\u8b49\u5b57\u865f\u9577\u5ea6\u61c9\u70ba10!");
			return false;
		} else {
			sPID = trim(sPID.toUpperCase());
			if(!chkPID_CHAR(sPID)) return false;
			
			var iChkNum = getPID_SUM(sPID);
			if (iChkNum % 10 != 0) {
				alert("\u8eab\u4efd\u8b49\u5b57\u865f\u4e0d\u7b26\u5408\u898f\u5247, \u8acb\u91cd\u65b0\u8f38\u5165\u6b63\u78ba\u7684\u8eab\u4efd\u8b49\u865f\u78bc!");
				return false;
				
				// check the last id must be what number!!
				
				//var iLastNum = sPID.substr(9, 1) * 1;
				//for (i=0; i<10; i++) {
				//	var xRightAlpNum = iChkNum - iLastNum + i;
				//	if ((xRightAlpNum % 10) ==0) {
				//		sMsg = "\u6700\u5f8c\u4e00\u500b\u6578\u61c9\u70ba\uff1a" + i;
				//		break;
				//	}
				//}
			}
		}
		
		return true;
	}
