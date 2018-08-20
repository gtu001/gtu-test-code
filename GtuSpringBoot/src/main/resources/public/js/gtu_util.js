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

//var jqueryTool = new JqueryTool();
//var information = new Information();

//--------------------------------------------------------------------
$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
}

//--------------------------------------------------------------------

// url parameter to map
function queryParameterToMap(){
	var map = {};
	var queryStr = window.location.search.substring(1);
	var vars = queryStr.split("&");
	for (i = 0; i < vars.length; i++) {
		var v = vars[i].split("=");
		map[v[0]] = decodeURIComponent(v[1]);
	}
	return map;
}

//--------------------------------------------------------------------


function RandomUtil() {
	this.rangeInt = function(start, end){
		end = end + 1;
		return Math.floor((Math.random() * end) + start);
	}
}

//--------------------------------------------------------------------