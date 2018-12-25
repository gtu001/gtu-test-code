// JavaScript Document
var Suggest = new (function(){
	
	this.URL = 2;
	this.LIST = 3;
	this.AJAX_LIST = 4;

	// debug switch
	var isDebug = false;
	this.setDebug = setDebug;
	function setDebug( status ){
		isDebug = !!status;
	}

	if (!window.CSS_locate_base || window.CSS_locate_base !== false) {
	    window.CSS_locate_base = false;
	    var css_regExp = new RegExp(/.css$/i);
	    var css = document.getElementsByTagName('link');
	    for (var i = 0 ; i < css.length ; i++) {
	        var cssHref = css[i].href + '';
	        if (css_regExp.test(cssHref)) {
	            var html_i = cssHref.indexOf('html');
	            window.CSS_locate_base = cssHref.substring(0, html_i);
	            break;
	        }
	    }
	}

	if (!window.HTML_locate_base || window.HTML_locate_base !== false) {
	    window.HTML_locate_base = location.href.split("servlet");
	    if (HTML_locate_base.length <= 1) {
	        HTML_locate_base = location.href.split("html");
	    }
	    if (HTML_locate_base.length > 1) {
	        HTML_locate_base = HTML_locate_base[0];
	    } else {
	        HTML_locate_base = false;
	    }
	}

    /* 引入CSS使用 */
	var css_filePath = 'html/CM/css/ui/suggest.css';
	if (window.CSS_locate_base) {
	    document.write('<link rel="stylesheet" type="text/css" href="' + CSS_locate_base + css_filePath + '" />');
	} else if (window.HTML_locate_base) {
	    var PROD_CssBase = HTML_locate_base.replace(/Web\//, 'Docs/');
	    document.write('<link rel="stylesheet" type="text/css" href="' + HTML_locate_base + css_filePath + '" />');
	    document.write('<link rel="stylesheet" type="text/css" href="' + PROD_CssBase + css_filePath + '" />');
	}
	
	// saved DOM Elements
	var actionItem;
	var actionSelection;
	var mainDiv = createDOM("DIV",null,{display:'none'},["suggest_main"],null,document.body );
	var queryResultUL = createDOM("ul",null,null,["suggest_list"],null,mainDiv);
	var queryResultDiv = createDOM("DIV",null,null,["suggest_info"],null,mainDiv, "information_bar" );
	
	// elem setting
	var Items_setting = {};
	var dontExcuteBlur = false;
	
	// saved values
	var ajaxDefaultUrl = "";
	var ajaxSearchMinLength = 4;
	var ajaxSearchGap = 2;
	var ajaxOptionsLimit = 10;
	var savedCookieLimit = 5;
	
	// ajax lock
	var ajaxing = null;
	
	var pageIdentify = location.href;
	var idf_position = pageIdentify.indexOf("Web/servlet/HttpDispatcher/");
	if(idf_position){
		pageIdentify = pageIdentify.substr(idf_position+27,9);
	}

	// old function
	function Local( elem , suggestArea , suggest_data , config ){
		// suggestArea not uesd
		add( elem , Suggest.LIST , suggest_data , config )
	}

	this.add = add;
	function add( elem , suggest_type , suggest_data , config ){
		if(!isDOMObject(elem)){
			elem = document.getElementById(elem);
			if(!elem){
				alert("找不到 id 為 [ "+elem+" ] 物件");
				return;
			}
		}
		
		var elem_id = elem.getAttribute('id');
		if(!elem_id){
			elem_id = elem.getAttribute('name');
			if(elem_id){
				elem_id = elem_id+'_'+new Date().getTime()+'_'+Math.floor((Math.random()*1000)+1); // 防止重複
				elem.setAttribute('id',elem_id);
			}
		}
		
		if(!elem_id){
			elem_id = 'suggest_'+new Date().getTime()+'_'+Math.floor((Math.random()*1000)+1); // 防止重複
			elem.setAttribute('id',elem_id);
		}
		
		if(Items_setting[elem_id]){
			alert("物件 [ "+elem_id+" ] 新增 suggest 重複宣告");
			return;
		}
		
		if(!isObject(config)){ 
			config = {}; 
		}else{
			var clone_config = {};
			for(var key in config){
				clone_config[key] = config[key];
			}
			config = clone_config;
		}
		
		var params = config.params;
		if(isFunction(params)){
			config.paramsFunction = params;
			params = {};
			config.params = params;
		}else if(!isObject(params)){ 
			params = {}; 
			config.params = params; 
		}else{
			var clone_params = {};
			for(var key in params){
				clone_params[key] = params[key];
			}
			params = clone_params;
		}
		
		setEventObserve(elem , "focus" , startSuggest );
		setEventObserve(elem , "click" , startSuggest );
		setEventObserve(elem , "keydown" , input_keyDown );
		setEventObserve(elem , "keyup" , input_keyUp );
		setEventObserve(elem , "blur" , function(e){ if(!dontExcuteBlur){excuteCallBackFunc(e); } } );
		
		params.suggestId = elem_id;
		config.elem = elem;
		
		Items_setting[elem_id] = config;
		
		switch(suggest_type){
			// 依照class處理，目前沒有使用
			case 1:
			case "1":
				config.suggest_type = 1;
				if(!isBasicType(suggest_data) && suggest_data.length != 9 && suggest_data.substr(2,1) == "_" ){
					alert("必須傳入長度9位的class名稱");
					return;
				}
				params["class"] = suggest_data;
				config.suggest_url = ajaxDefaultUrl;
				break;
			// 初始時取得ajax資料
			case 2:
			case "2":
				config.suggest_type = 2;
				if(!isBasicType(suggest_data)){
					alert("必須傳入ajax url");
					return;
				}
				config.suggest_url = suggest_data;
				break;
			// 動態多次取得ajax資料
			case 4:
			case "4":
				config.suggest_type = 4;
				if(!isBasicType(suggest_data)){
					alert("必須傳入ajax url");
					return;
				}
				config.suggest_url = suggest_data;
				break;
			// 傳入固定值
			default:
				var old_supt = document.getElementById(suggest_type);
				if(old_supt){
					old_supt.parentNode.removeChild(old_supt);
				}
				config.suggest_type = 3;
				if(!isArray(suggest_data)){
					alert("必須傳入建議資料");
					return;
				}
				config.suggest_result = suggest_data;
				break;
		}
		
		config.prefix = config.prefix === true;
		config.ignoreCase = config.ignoreCase !== true;
		config.noCookies = config.noCookies === true;
		config.startLength = isNumeric(config.startLength) ? (config.startLength-0) : ajaxSearchMinLength;
		config.searchGap = isNumeric(config.searchGap) ? (config.searchGap-0) : ajaxSearchGap;
		params.prefix = config.prefix ? "Y" : "N";
		params.ignoreCase = config.ignoreCase ? "Y" : "N";
		

		elem.prototype = Object.extend(elem,{
			setValue:function(value){
				this.value = value;
				startSuggest(this);
				excuteCallBackFunc(actionItem , 100);
			}
		});
		
	}
	
	this.clear = clear;
	function clear(elem){
		
		var elem_id = elem;
		if(isDOMObject(elem)){
			elem_id = elem.getAttribute('id');
		}
		
		var config = Items_setting[elem_id]; 
		
		if(!config){
			alert("找不到物件 id 為 [ "+elem_id+" ] ，請確認該物件已經有新增Suggest。");
			return;
		}
		
		config.prevSearch = null;
		config.suggest_result = null
		config.ajaxSearch = -1;
		
		closeSuggest();
		
		
	}
	
	
	function startSuggest(e){
		if(actionItem){
			closeSuggest();
		}
		var target = getEventTarget(e);
		if(!target){ debug("no target to start"); return;}
		addClassName(target.parentNode , "suggestParent");
		actionItem = target;
		dontExcuteBlur = false;
		updateSuggest();
	}
	
	function updateSuggest(){	
		var target_id = actionItem.getAttribute("id");
		var config = Items_setting[target_id];
		
		var value = trim(actionItem.value||"");
		var valueLength = getByteLength(value);
		var suggestStartLength = config.startLength;
		// debug("now value Length : " + valueLength + " ajax Limit length : " +
		// suggestStartLength );
		
		if( valueLength == 0 ){
			getSearchHistory( null , config );
		}else if( valueLength < suggestStartLength ){
			getSearchHistory(value , config );
		}else if(config.suggest_type == 3){
			createSuggestList( config.suggest_result , config );
		}else if(config.suggest_type == 4){
			if(config.suggest_result){
				createSuggestList( config.suggest_result , config );
			}else{
				getAjaxData(config);
			}
		}else{
			getAjaxData(config);
		}
	}
	
	
	function getAjaxData( config , suggestValue ){
		
		if(!window.Prototype){ alert("請先import prototype.js"); return; }
		
		var elem = config.elem;
		var prefix = config.prefix;
		var ignoreCase = config.ignoreCase;
		suggestValue = suggestValue || elem.value;
		if(ignoreCase) { suggestValue = suggestValue.toLowerCase(); }
		
		var queryLength = getByteLength(suggestValue);
		if(suggestValue == config.prevSearch ){ 
			debug("query rule not change : " + suggestValue);
			openSuggest(elem);
			return; 
		}
		if(isArray(config.suggest_result)){
			var isMatch = queryLength >= getByteLength(config.ajaxSearch) ? suggestValue.indexOf(config.ajaxSearch) : -1;
			if(isMatch >= 0) {
				createSuggestList( config.suggest_result , config );
				debug("find : "+suggestValue+"   from : " + config.ajaxSearch);
				return;
			}
		}
		
		// ajax lock
		if(ajaxing){
			if(elem == ajaxing) { 
			    debug("** ajax locked with same target");
			    getSearchHistory(suggestValue, config,"後端查詢建議資料中...");
				return;
			}else{
				debug("** ajax locked with different target");
				ajaxing = elem;
				return;
			}
		}
		ajaxing = elem;

		getSearchHistory(suggestValue, config,"後端查詢建議資料中...");
		
		if( config.searchGap > 0 ){
			queryLength = queryLength - config.searchGap;
			if(queryLength < config.startLength){
				queryLength = config.startLength;
			}
			while(queryLength < getByteLength(suggestValue)){
				suggestValue = suggestValue.substr( 0 , suggestValue.length -1 );
			};
		}
		
		config.ajaxSearch = suggestValue;
		
		var url = config.suggest_url;
		var params = config.params;
		if(isFunction(config.paramsFunction)){
			var dync_params = config.paramsFunction();
			if(isObject(dync_params)){
				var new_params = {};
				for( k in params){ new_params[k] = params[k]; }
				for( k in dync_params){ new_params[k] = dync_params[k]; }
				params = new_params;
			}
		}
		params["suggestValue"] = suggestValue;
		
		debug("do AJAX : "+ elem.id +" / "+suggestValue);
		CSRUtil && CSRUtil.defaultAjaxHandler && Ajax.Responders.unregister(CSRUtil.defaultAjaxHandler);
		new Ajax.Request(url, {
			method: 'post',
			parameters: params,
			 onSuccess: function(response) {
				var resp = response.responseJSON;
				if(CSRUtil.isSuccess(resp)){
					if(ajaxing){
						var suggestResult = resp.suggestResult;
						if(isArray(suggestResult)){
							var option_key = isString(config.option_key) && config.option_key;
							suggestResult.sort( function(x, y){
								if(option_key){
									x = isObject(x) ? x[option_key] : null;
									y = isObject(y) ? y[option_key] : null;
								}
								if (!isBasicType(y)){
									return -1;
								}
								if (!isBasicType(x)){
									return 1;
								}
								
								var indexOf_x = x.indexOf(suggestValue);
								var indexOf_y = y.indexOf(suggestValue);
								if( indexOf_x == indexOf_y){
									return 	x > y ? 1 : -1;
								}
								return  indexOf_x > indexOf_y ? 1 : -1 ;
							});
							config.suggest_result = suggestResult;
							debug("save list from AJAX : " + suggestResult.length);
						}else{
							config.suggest_result = null;
							debug("save list from AJAX : 0");
						}
						
						if(elem == ajaxing){
							debug("++ ajax match with actionItem, create List");
							if (config.suggest_result) {
							    createSuggestList(config.suggest_result, config);
							}
							// ajax unlock
							ajaxing = null;
						}else{
							debug("++ ajax not match with actionItem, recall getAjaxData");
							// ajax unlock
							var new_config = Items_setting[ajaxing.id]; 
							ajaxing = null;
							getAjaxData(new_config);
						}
					}
				}
			}
		});
		
		CSRUtil && CSRUtil.defaultAjaxHandler && Ajax.Responders.register(CSRUtil.defaultAjaxHandler);
	}
	
	function input_keyDown(e){
		var keynum;
		if(window.event){
			// IE
			keynum = e.keyCode;
		}else if(e.which) {
			// Netscape/Firefox/Opera
			keynum = e.which;
		}
		
		switch(keynum){
			case 9: // TAB
				if(dontExcuteBlur){
					excuteCallBackFunc(e); 
				}
				cancelEventBubble(e);
				break;
			case 13: // ENTER
				if(actionSelection){
					setToActionItem(actionSelection);
					dontExcuteBlur = true;
				}
				cancelEventBubble(e);
				break;
			case 38: // UP
				if(mainDiv.style.display == 'block'){
					if(actionSelection && actionSelection.previousSibling){
						removeClassName(actionSelection,"selectItem");
						actionSelection = actionSelection.previousSibling;
						addClassName(actionSelection,"selectItem");
					}else if( queryResultUL.hasChildNodes()){
						if(actionSelection){
							removeClassName(actionSelection,"selectItem");
						}
						actionSelection = queryResultUL.lastChild;
						addClassName(actionSelection,"selectItem");
					}
				}
				cancelEventBubble(e);
				break;
			case 40: // DOWN
				if(mainDiv.style.display == 'block'){
					if(actionSelection && actionSelection.nextSibling){
						removeClassName(actionSelection,"selectItem");
						actionSelection = actionSelection.nextSibling;
						addClassName(actionSelection,"selectItem");
					}else if( queryResultUL.hasChildNodes()){
						if(actionSelection){
							removeClassName(actionSelection,"selectItem");
						}
						actionSelection = queryResultUL.firstChild;
						addClassName(actionSelection,"selectItem");
					}
				}
				cancelEventBubble(e);
				break;
		}
	}
	
	function input_keyUp(e){
		var keynum;
		if(window.event){
			// IE
			keynum = e.keyCode;
		}else if(e.which) {
			// Netscape/Firefox/Opera
			keynum = e.which;
		}
		
		switch(keynum){
			case 27: // ESC
				closeSuggest();
				cancelEventBubble(e);
				return;
			case 13: // ENTER
			case 38: // UP
			case 40: // DOWN
				cancelEventBubble(e);
				return;
			case 37: // left
			case 39: // right
			case 35: // HOME
			case 36: // END
				return;	
			default:
				debug("press key :" + keynum);
		}
		startSuggest(e);
	}
	
	function openSuggest( target ){
		// 將資料視窗顯示
		if(!target){ target = actionItem; }
		if(target == mainDiv.previousSibling && mainDiv.style.display == 'block'){ 
			return; 
		}
		/*
		 * if(!queryResultUL.hasChildNodes() ){ debug("No suggest! close
		 * Window"); closeSuggest(); return; }
		 */
		
		target.parentNode.insertBefore(mainDiv,target);
		mainDiv.style.minWidth = ( target.offsetWidth - 2) + "px";
		mainDiv.style.top = (target.offsetTop + target.offsetHeight + 1 ) + "px";
		mainDiv.style.left = target.offsetLeft + "px";
		mainDiv.style.display = 'block';
	}
	
	function closeSuggest( e ) {
	    var target = actionItem;
	    if (target) {
	        removeClassName(target.parentNode, "suggestParent");
	        var target_id = target.getAttribute("id");
	        var config = Items_setting[target_id];
	        config && (config.prevSearch = null);
	    }
	    mainDiv.style.display = 'none';

        actionItem = null;
	    
		var ajaxingCover = window.CSRUtil && CSRUtil.createCoverPage({ 'id': '_suggestAjaxing' });
		if (ajaxingCover) { ajaxingCover.hide(); }
	}
	
	function excuteCallBackFunc(e, waitToClose, clicklFromHistory ) {

	    var target = getEventTarget(e);
		var target_id = target.getAttribute("id");
		var config = Items_setting[target_id];
		if (!config || !isFunction(config.callBackFunc)) {
		    closeSuggest();
		    return;
		}

		if (ajaxing) {
		    var ajaxingCover = window.CSRUtil && CSRUtil.createCoverPage({ 'id': '_suggestAjaxing' });
		    if (ajaxingCover) { ajaxingCover.show().resizeCoverPage(); }
		    var caller = this;
		    setTimeout(function () { excuteCallBackFunc.call(caller, target, waitToClose); }, 100);
		    return;
		}

		var suggest_type = config.suggest_type;
		var suggest_result = config.suggest_result;
		if ((clicklFromHistory === true || !suggest_result || !suggest_result.length) && (suggest_type == 2 || suggest_type == 4)) {
		    getAjaxData(config);
		    return;
		}

		var value = target.value;
		
		var option_key = isString(config.option_key) && config.option_key;
		var ignoreCase = config.ignoreCase;
		var select_obj;
		
		if(value && isArray(suggest_result)){
			var input_value = ignoreCase ? value.toLowerCase() : value;
			for( var i = 0 ; i < suggest_result.length ; i++){
				var testValue = suggest_result[i];
				if(option_key){
					testValue = isObject(testValue) ? testValue[option_key] : null;
				}
				if(!testValue) { continue; }
				ignoreCase && (testValue = testValue.toLowerCase());
				if( testValue == input_value ){
					select_obj = suggest_result[i];
					break;
				}
			}
		}
		if(select_obj){
			config.callBackFunc(target , select_obj);
		}else{
			config.callBackFunc(target);
		}

		if (isNumeric(waitToClose)) {
		    setTimeout(closeSuggest, 100);
		} else {
		    closeSuggest();
		}
	}
	
	function setToActionItem( selected_item ){
		if(!actionItem )	{ return; }
		var value = selected_item.getAttribute("item_value");
		value = isBasicType(value) ? value : "";
		actionItem.value = value;
		setSearchHistory( value , Items_setting[actionItem.id] );
		excuteCallBackFunc(actionItem, 50, selected_item.getAttribute('_history')=='Y');
		selectActionItem();
	}
	
	function selectActionItem(){
		if(!actionItem ){ return; }
		var value = actionItem.value;
		var valueLength = value.length;
		if (actionItem.setSelectionRange) {
			actionItem.setSelectionRange(valueLength,valueLength);
		}else if (actionItem.createTextRange) {
			var range = actionItem.createTextRange();
			range.collapse(true);
			range.moveEnd('character', valueLength);
			range.moveStart('character', valueLength);
			range.select();
		}
		actionItem.focus();
	}
	
	function itemMouseMoveOver(e){
		var target = getEventTarget(e);
		if(!target){ return; }
		dontExcuteBlur = true;
		actionSelection = target;
		addClassName(target,"selectItem");
	}
	
	function itemMouseMoveOut(e){
		var target = getEventTarget(e);
		if(target && actionSelection == target){
			actionSelection = null;
			removeClassName(target,"selectItem");
			dontExcuteBlur = false;
		}
	}
	
	function itemMouseClick(e){
		var target = getEventTarget(e);
		if(!target){ return; }
		setToActionItem( target );
		cancelEventBubble(e);
	}
	
	
	function clearAllSuggest(){
		while(queryResultUL.hasChildNodes()){
			queryResultUL.removeChild(queryResultUL.firstChild);
		}
	}

	function createSuggest( value , display , serialNo ){
		return  createDOM("li",{'item_value':value,'sn':serialNo},null,["suggest_li"],{ "click":itemMouseClick , mouseover:itemMouseMoveOver , mouseout:itemMouseMoveOut },queryResultUL,display);
	}
	
	function createSuggestList( data_list , config ){
		var elem = config.elem;
		if( elem != actionItem){ return; }
		if( !isArray(data_list) || data_list.length == 0 ){ return; }
		
		clearAllSuggest();
		var value_in = elem.value;
		var displayNum = isNumeric(config.dispMax) ?  config.dispMax : ajaxOptionsLimit;
		var option_key = isString(config.option_key) && config.option_key;
		var remark_key = isString(config.remark_key) && config.remark_key;
		var simple_mode = !option_key;
		
		var prefix = config.prefix;
		var ignoreCase = config.ignoreCase;
		if(value_in && ignoreCase) { value_in = value_in.toLowerCase(); }
		
		config.prevSearch = value_in;
		
		debug("do create List with pattern: "+ value_in);
		
		var dataCount = 0;
		for(var i = 0 ; i < data_list.length ; i++){
			var testData = data_list[i];
			var testValue = simple_mode ?  testData : (isObject(testData) ? testData[option_key] : "" );
			if(isBasicType(testValue) && testValue != 'undefined' && testValue !== "" ){
				var displayValue = testValue;
				if(ignoreCase) { testValue = testValue.toLowerCase(); }
				var isMatch = value_in ? testValue.indexOf(value_in) : 0;
				if( prefix ? ( isMatch == 0 ) : isMatch >= 0 ){
					var remark = !simple_mode && testData[remark_key];
					remark = (remark && isBasicType(remark) && remark != 'undefined' && remark !== "") ? "<div>"+remark+"</div>" : "";
					if(dataCount < displayNum){
						createSuggest(displayValue,displayValue+remark , i);
					}
					dataCount++;
				}
			}
		}
		queryResultDiv.innerHTML = "查詢共有"+dataCount+"筆相符資料";
		
		openSuggest( elem );
	}
	
	function getSearchHistory( value_in , config , noCookieMsg ){
		clearAllSuggest();
		config.prevSearch = null;
		if (config.noCookies) {
		    selectActionItem();
		    if (noCookieMsg) {
		        queryResultDiv.innerHTML = noCookieMsg;
		        mainDiv.style.display = '';
		    } else {
		        mainDiv.style.display = 'none';
		    }
			return false;
		}
		
		var actionId = "suggest_"+pageIdentify +"_"+actionItem.getAttribute("id");
		var savedData = (getCookie(actionId) || "").split(",");
		
		var prefix = config.prefix;
		var ignoreCase = config.ignoreCase;
		if(value_in && ignoreCase) { value_in = value_in.toLowerCase(); }
		
		debug("do get Cookie with pattern: "+ value_in);
		
		var dataCount = 0;
		for(var i = 0 ; i < savedData.length ; i++){
			var testValue = trim(savedData[i]);
			if(isBasicType(testValue) && testValue != 'undefined' && testValue !== "" ){
				var displayValue = testValue;
				if(ignoreCase) { testValue = testValue.toLowerCase(); }
				var isMatch = value_in ? testValue.indexOf(value_in) : 0;
				if( prefix ? ( isMatch == 0 ) : isMatch >= 0 ){
				    createSuggest(testValue, testValue, i).setAttribute('_history', 'Y');
					dataCount++;
				}
			}
		}
		queryResultDiv.innerHTML = "歷史輸入共"+dataCount+"筆資料";
		
		openSuggest(config.elem);

		return true;
	}
	
	function setSearchHistory(value , config){
		if(!config.noCookies && value){
			var ignoreCase = config.ignoreCase;
			
			var actionId = "suggest_"+pageIdentify +"_"+config.elem.getAttribute("id");
			var savedData = (getCookie(actionId) || "").split(",");
			var newSavedData = value;
			var nowSaved = 1;
			for(var i = 0 ; i < savedData.length && nowSaved<savedCookieLimit ; i++){
				var testData = trim(savedData[i]);
				if(isBasicType(testData) &&  testData != value ){
					newSavedData = newSavedData + "," + testData;
					nowSaved++;
				}
			}
			setCookie(actionId,newSavedData,120);
		}
	}
	
	
	
	
	// utilitys
	function debug(msg){
		if(isDebug && window.console){
			console.log(msg);
		}
	}
	
	
	function setCookie(name,value,cookieHours){
		var exp  = new Date();
		exp.setTime(exp.getTime() + (cookieHours||8)*60*60*1000);
		document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
	}
	
	function getCookie(name){
		var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
		return (arr != null) ? unescape(arr[2])||"" : "" ;
	}
	
	// 取得母元素
	function getParentByTagName( node , tagName ){
		if(!isDOMObject(node) || !isBasicType(tagName)){return null;}
		tagName = tagName.toUpperCase();
		do{
			node = node.parentNode;
			if( !node||node.tagName == "BODY"){return null;}
		}while(node.tagName!=tagName);
		return node;
	}
	
	// 事件處理
	function setEventObserve( elem , eventName , func){
		if( !(isDOMObject(elem) || elem==window || elem==document )  || !isString(eventName) || !isFunction(func) ){ return; }
		if(elem.removeEventListener) {						// DOM2接口 with
															// bubble
			elem.addEventListener(eventName, func, false);  
		} else if(elem.detachEvent) {					// IE DOM2接口 with bubble
			elem.attachEvent('on'+eventName, func);  
		} else {											// DOM0接口 without
															// bubble
			if( window.Prototype){
				Event.observe(elem, eventName, func);
			}else{
				elem['on'+eventName]=func; 
			}
		}
	}
	
	function stopEventObserving( elem , eventName , func  ){
		if( !(isDOMObject(elem) || elem==window || elem==document )  || !isString(eventName) ){ return; }
		if(elem.removeEventListener) {						// DOM2接口 with
															// bubble
			elem.removeEventListener(eventName, func);  
		} else if(elem.detachEvent) {						// IE DOM2接口 with
															// bubble
			elem.detachEvent('on'+eventName, func);
		} else {											// DOM0接口 without
															// bubble
			if( window.Prototype){
				Event.stopObserving(elem, eventName);
			}else{
				elem['on'+eventName]=null; 
			}
		}
	}
	
	function cancelEventBubble(e){ 
		if (!e) 
		  e = window.event; 
		// IE9 & Other Browsers
		if (e.stopPropagation) { 
		  e.stopPropagation(); 
		} 
		// IE8 and Lower
		else { 
		  e.cancelBubble = true; 
		} 
	}
	
	function getEventTarget(e) {
	    if (!e) e = window.event;
	    if (isDOMObject(e)) {
	        return e;
	    }
		var target = e.target || e.srcElement;
		if(isDOMObject(target)){
			return target;
		}
		return false;
	}
	
	// 基礎判斷
	function isDOMObject(node, allowFragment) {
		// nodeType == 1 --> DOM
		// nodeType == 3 --> TextNode
		// nodeType == 11 --> Fragment
		return !!(node && ((node.tagName && node.nodeType === 1) || node.nodeType === 3 || (allowFragment && node.nodeType === 11)));
	}
	
	function isString( node ){
		return !!(isBasicType( node ) && getType(node) === '[object String]');
	}
	
	function isNumeric(node){ return isBasicType( node )&&(/^-?(0|[1-9]\d*|(?=\.))(\.\d+)?$/.test(node)); } 
	
	function isBasicType( node ){
		var type = typeof(node);
		return !!( type === 'string' || type ==='number' || type === 'boolean' );
	}
	
	function isArray( node ){
		return !!(node && getType(node) === '[object Array]' && isNumeric( node.length ));
	}
	 
	function isObject(node){
		return !!(node && getType(node) == '[object Object]' && !isArray(node) && !isFunction(node) );
	}
	
	function isFunction( node ){
		return !!(node && getType(node) === '[object Function]');
	}
	
	function isInArray( array , obj) {
		if(!isArray(array)){ return -1; }
		for (var i = 0 ; i < array.length ; i++) {
			if ((""+array[i]) === (""+obj) ) { return i; }
		}
		return -1;
	}	
		
	function getType(node){
		if(typeof(node) === 'undefined' ) {return "undefined"; }
		return Object ? Object.prototype.toString.apply(node) : "";
	}
	
	function getByteLength( value ) {
		var arr = value.match(/[^\x00-\xff]/ig);
		return  arr == null ? value.length : value.length + arr.length;
	}
	
	
	// class 判斷
	function addClassName( node , className){
		if( isBasicType(className) && !hasClassName( node , className)){
			node.className = trim((node.className||"") +" "+ className);
		}
	}
	
	function getClassName( node ){
		var classes = node.className;
		if(!isBasicType(classes)) { return []; }
		classes = trim(classes.replace(/\s{2,}/g," "));
		return stringToArray(classes ," ");
	}
	
	function removeClassName(node , compare ){
		if(!isBasicType(compare)){ return false; }
		var classes = node.className;
		if(!isBasicType(classes)) { return false; }
		compare = compare.replace(/\\/gi,"\\\\");
		
		var reg=new RegExp("(^"+compare+"\\s)|(\\s"+compare+"\\s)|(\\s"+compare+"$)|(^"+compare+"$)","gi");
		node.className = classes.replace(reg," ").replace(/\s\s/gi," ");
	}
	
	function hasClassName(node , compare ){
		if(!isDOMObject(node) || !isBasicType(compare)){ return false; }
		var classes = node.className;
		if(!isBasicType(classes)) { return false; }
		compare = compare.replace(/\\/gi,"\\");
		
		var reg=new RegExp("(^"+compare+"\\s)|(\\s"+compare+"\\s)|(\\s"+compare+"$)|(^"+compare+"$)","gi");
		
		return classes.match(reg) != null;
	} 
	
	function stringToArray( stringValue , splitChar ){
		if(isArray(stringValue)){ return stringValue; }
		if(!isBasicType(stringValue)){ return [ trim(stringValue) ];}
		var strings = stringValue.split(splitChar);
		var rtnArray = [];
		for(var i = 0 ; i < strings.length ; i = i + 1 ){
			var str = trim(strings[i]);
			if (isBasicType(str) && str != "undefined") {
				rtnArray.push(str);
			}
		}
		return rtnArray;
	}
	
	// 基礎類別
	function trim(value)
	{
		return isString(value) ? value.replace(/(^\s*)|(\s*$)/g, "").replace(/^&nbsp;|&nbsp;$/g,"") : value; 
	}
	
	// 元素操作
	function createDOM( node , attrs , styles , classes , events , appendAt , content , createDocument ){
		
		if(!isDOMObject( node )){
			node = (createDocument||document).createElement(node);	
		}
		
		if( isObject(attrs) ){
			for( var key in attrs ){
				if(isBasicType(attrs[key])){
					node.setAttribute( key , attrs[key] );
				}
			}
		}
		
		if( isObject(styles) ){
			for( var key in styles ){
				if(isBasicType(styles[key])){
					node.style[key] =  styles[key];
				}
			}
		}
				
		if( isArray(classes) ){
			for( var i = 0  ; i < classes.length ; i = i + 1  ){
				addClassName( node , classes[i] );
			}
		}
		
		if( isObject(events) ){
			for( var key in events ){
				 setEventObserve( node , key , events[key] );
			}
		}
		
		if(content){
			node.appendChild(getContent(content));
		}
		
		if(isDOMObject( appendAt , true )){
			appendAt.appendChild(node);
		}
		
		return node;
	}
	
	function getContent(testNode, whenNULL, function_params, function_input_params_part) {
	
		if (isDOMObject(testNode, true)) {
			return testNode;
		}
	
		if(isFunction(testNode)){
			if(function_params){
				if( function_input_params_part && isArray(function_params) ){
					testNode = testNode(function_params[0],function_params[1],function_params[2],function_params[3],function_params[4],function_params[5]);
				}else{
					testNode = testNode(function_params);
				}
			}else{
				testNode = testNode();
			}
		}
		if (!isDOMObject(testNode,true)) {
			if(!isBasicType(testNode)){
				testNode = isBasicType(whenNULL) ? whenNULL : "&nbsp;" ;
			}
			var frag = createDOM("span");
			frag.innerHTML = testNode;
			testNode = document.createDocumentFragment();
			while(frag.hasChildNodes()){
				testNode.appendChild(frag.firstChild);
			}
		}
		return testNode;
	}
	
	function createFragment(elems) {
		var fragment = document.createDocumentFragment();
		if (isArray(elems)) {
			for (var i = 0 ; i < elems.length ; i = i + 1) {
				var elem = getContent(elems[i]);
				if (elem) {
					fragment.appendChild(elem);
				}
			}
		}
		return fragment;
	}
	
	function cloneObject(orgObject) {
		var returnObject;
		if(isArray(orgObject)){
			returnObject = [];
			for(var i = 0 ; i < orgObject.length ; i = i + 1 ){
				returnObject[i]=cloneObject(orgObject[i]);
			}
		}else if(isDOMObject(orgObject)){
			returnObject = null;
		}else if(isObject(orgObject)){
			returnObject = {};
			for(var key in orgObject){
				returnObject[key]=cloneObject(orgObject[key]);
			}
		}else{
			returnObject = orgObject;
		}
		return returnObject;
	}

} )();

function startSuggest(){ /* 配合舊版程式，已無作用 */ }

function SuggestStart(url, reqlist, callbackFun, option){
	if( !reqlist || reqlist.length == 0){return;}
	new Ajax.Request(url,{
		method:'post',
	  	parameters: {reqlist:Object.toJSON(reqlist)},
	  	onCreate:showProgress,
	  	onSuccess:function(transport){
	  		stopProgress();
	  		var json=transport.responseText.evalJSON();
	  		Sys.check(json,function(status){
	  			if(status){ 
	  				if(!callbackFun){
		  				SuggestStartAfter(reqlist,json.dataMap, option);
		  			}else{
		  				callbackFun(reqlist,json.dataMap, option);
		  			}		
	  			}
		  	});
	  	},
	  	onFailure: Sys.SystemError
	});
}

function SuggestStartAfter(reqlist, dataMap, option){
	if( !dataMap || dataMap.length == 0){return;}
	for( var i=0; i<reqlist.length; i++){
		var req_setting = reqlist[i];
		var suggestList = dataMap[req_setting.CODE ];
		var suggestNode = reqlist[i].INPUT;
		if(suggestNode && suggestList){
			Suggest.add(suggestNode, Suggest.LIST, suggestList, option );			
		}
	}
}