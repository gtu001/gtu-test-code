	//<script language='javascript' src='basicJsTools.js' type=""></script>

	function showFormElements(){
		for(var ii=0;ii<window.document.forms.length;ii++){
				alert("FORM\r\n"+
					"name:\t"+window.document.forms[ii].name +"\r\n"+
					"action:\t"+window.document.forms[ii].action +"\r\n"+
					"constructor:\t"+window.document.forms[ii].constructor +"\r\n"+
					"onreset:\t"+window.document.forms[ii].onreset +"\r\n"+
					"encoding:\t"+window.document.forms[ii].encoding +"\r\n"+
					"method:\t"+window.document.forms[ii].method +"\r\n"+
					"onsubmit:\t"+window.document.forms[ii].onsubmit +"\r\n"+
					"target:\t"+window.document.forms[ii].target +"\r\n"+
					"elements:\t"+window.document.forms[ii].elements +"\r\n"+
					"length:\t"+window.document.forms[ii].length +"\r\n");
			for(var i=0;i<window.document.forms[ii].elements.length;i++){
				alert(
					"type:\t"+window.document.forms[ii].elements[i].type +"\r\n"+
					"id:\t"+window.document.forms[ii].elements[i].id +"\r\n"+
					"name:\t"+window.document.forms[ii].elements[i].name +"\r\n"+
					"value:\t"+window.document.forms[ii].elements[i].value +"\r\n"
					);
			}
		}
	}
	
	
	function showDelChecked(fom){
		var checkItem = '';
		alert(fom.elements.length);
		for (var i=0;i<form1.elements.length;i++,checkItem+=',') { 
			if(fom.elements[i].type == 'checkbox' && fom.elements[i].checked)
				checkItem += fom.elements[i].name;
		} 
		alert(checkItem);
	}