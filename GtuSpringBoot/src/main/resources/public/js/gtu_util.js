function JqueryTool(){
	
	//用來放 $.get().fail(jqueryTool.ajaxFailFunc);
	//--------------------------------------------------------------------
	this.ajaxFailFunc = function(jqXHR, textStatus, errorThrown) {//
		var jqXHRJson = jqXHR['responseJSON'];
		var msgInJqXHR = "";
		for ( var ii in jqXHRJson) {
			msgInJqXHR += ii + " : " + jqXHRJson[ii] + "\n";
		}
		alert(//
		msgInJqXHR + //
		"------------------------------\n" + 
		"textStatus : " + textStatus + "\n" + //
		"errorThrown : " + errorThrown //
		);
	}
	//--------------------------------------------------------------------
};

function Information() {
	
	//檢查畫面有多少tag
	//--------------------------------------------------------------------
	this.findAllChildren = function(tag, map) {
		var func = this.findAllChildren;
		$(tag).children().each(function(d, v) {
			var tagName = $(v).prop("tagName");
			var name = $(v).attr("name");
			var id = $(v).attr("id");

			var arry = new Array();
			if (tagName in map) {
				arry = map[tagName];
			}

			var dtl = {};
			if(name != undefined){
				dtl['name'] = name;
			}
			if(id != undefined){
				dtl['id'] = id;
			}
			arry.push(dtl);

			map[tagName] = arry;
			if($(v).children().length > 0){
				func(v, map);
			}
		});
	};
	
	var tagMap = {};
	this.findAllChildren($("html"), tagMap);
	this.tagMap = tagMap;
	
	this.getTagInfo = function(tagName){
		tagName = tagName.toUpperCase();
		if(tagName in this.tagMap){
			return this.tagMap[tagName];
		}
		throw Error("tagName 找不到 : " + tagName + " --> all : " + JSON.stringify(this.tagMap));
	}
	
	//--------------------------------------------------------------------
}

var jqueryTool = new JqueryTool();
var information = new Information();