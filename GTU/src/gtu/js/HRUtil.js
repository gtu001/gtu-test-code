	// This method must let <target text tag> at parent.children[0] position
	// argument at HTML put 'this'
	function showHRCode(obj){
		var strFeatures = "dialogWidth:650px;dialogHeight:520px";
		str = window.showModalDialog("HRServiceItem.do?method=findAll" ,
			obj.parentNode.children[0], strFeatures);
			
		if(str == null ||  typeof(str) == "undefined"){
			str = "";
		}
		obj.parentNode.children[0].value = str;
	}
	
	