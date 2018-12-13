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
var css_filePath = 'html/CM/css/ui/InputUtility.css';
if (window.CSS_locate_base) {
    document.write('<link rel="stylesheet" type="text/css" href="' + CSS_locate_base + css_filePath + '" />');
} else if (window.HTML_locate_base) {
    var PROD_CssBase = HTML_locate_base.replace(/Web\//, 'Docs/');
    document.write('<link rel="stylesheet" type="text/css" href="' + HTML_locate_base + css_filePath + '" />');
    document.write('<link rel="stylesheet" type="text/css" href="' + PROD_CssBase + css_filePath + '" />');
}


window.InputUtility = { };
    
InputUtility['utility'] = new (function(){
    //基礎判斷
    this.isElement = isElement;
    function isElement(node, allowFragment) {
        //nodeType == 1 --> DOM
        //nodeType == 3 --> TextNode
        //nodeType == 11 --> Fragment
        return !!(node && ((node.tagName && node.nodeType === 1) || node.nodeType === 3 || (allowFragment && node.nodeType === 11)));
    };
    
    this.isString = isString;
    function isString(node) {
        return !!(isBasicType(node) && getType(node) === '[object String]');
    };

    this.isNumeric = isNumeric;
    function isNumeric(node) { return isBasicType(node) && (/^-?(0|[1-9]\d*|(?=\.))(\.\d+)?$/.test(node)); }

    this.isBasicType = isBasicType;
    function isBasicType(node) {
        var type = typeof (node);
        return !!(type === 'string' || type === 'number' || type === 'boolean');
    };

    this.isFunction = isFunction;
    function isFunction(node) {
        return !!(node && getType(node) === '[object Function]');
    };

    this.isPlainObject = function (node) {
        return !!(node && getType(node) === '[object Object]');
    };

    this.isArray = function (node) {
        return !!(node && getType(node) === '[object Array]');
    };
    
    this.getType = getType;
    function getType(node) {
        if (typeof (node) === 'undefined') { return "undefined"; }
        return Object ? Object.prototype.toString.apply(node) : "";
    };

    this.hasAttribute = hasAttribute;
    function hasAttribute(elem, attr_name) {
        if (!isElement(elem)) { return false; }
        if (isFunction(elem.hasAttribute)) {
            return elem.hasAttribute(attr_name);
        }
        return !!elem[attr_name];
    };

    // 事件處理
    this.setEventObserve = setEventObserve;
    function setEventObserve(elem, eventName, func) {
        if (!(isElement(elem) || elem == window || elem == document) || !isString(eventName) || !isFunction(func)) { return; }
        
        if (window.Prototype) {
                Event.observe(elem, eventName, func);
        }else if (window.jQuery) {
                window.jQuery(elem).on( eventName, func );
        }else if (elem.removeEventListener) { //DOM2接口 with bubble
            elem.addEventListener(eventName, func, false);
        } else if (elem.detachEvent) { //IE DOM2接口  ˍ bubble
            elem.attachEvent('on' + eventName, func);
        } else { //DOM0接口 without bubble
            elem['on' + eventName] = func;
        }
    };

    this.fireEventObserving = fireEventObserving;
    function fireEventObserving(elem, eventName) {
        if (!isElement(elem)) { return; }
        try {
            elem[eventName]();
        } catch (exception) {
            try {
                var a = document.createEvent('MouseEvents');
                a.initEvent(eventName, true, true);
                elem.dispatchEvent(a);
            } catch (exception) {
                try {
                    elem.fireEvent('on' + eventName);
                } catch (exception) {
                    try {
                        Event.fire(elem, eventName);
                    } catch (exception) {
                        try {
                            elem['on' + eventName]();
                        } catch (exception) {
                        }
                    }
                }
            }
        }
    }

    this.getEventTarget = getEventTarget;
    function getEventTarget(e) {
        if (!e) e = window.event;
        var target = e.target || e.srcElement;
        if (isElement(target)) {
            return target;
        }
        if (isElement(e)) {
            return e;
        }
        return false;
    };

    this.trim = trim;
    function trim(value){
		return isString(value) ? value.replace(/(^\s*)|(\s*$)/g, "").replace(/^&nbsp;|&nbsp;$/g,"") : value; 
	}

    this.addClassName = addClassName;
    function addClassName( node , className){
        if(!isElement(node)){ return; }
		if( isBasicType(className) && !hasClassName( node , className)){
			node.className = trim((node.className||"") +" "+ className);
		}
	}
	
    this.getClassName = getClassName;
	function getClassName( node ){
        if(!isElement(node)){ return []; }
		var classes = node.className;
		if(!isBasicType(classes)) { return []; }
		classes = trim(classes.replace(/\s{2,}/g," "));
		return stringToArray(classes ," ");
	}
	
    this.removeClassName = removeClassName;
	function removeClassName(node , compare ){
        if(!isElement(node)){ return false; }
		if(!isBasicType(compare)){ return false; }
		var classes = node.className;
		if(!isBasicType(classes)) { return false; }
		compare = compare.replace(/\\/gi,"\\\\");
		
		var reg=new RegExp("(^"+compare+"\\s)|(\\s"+compare+"\\s)|(\\s"+compare+"$)|(^"+compare+"$)","gi");
		node.className = classes.replace(reg," ").replace(/\s\s/gi," ");
	}
	
    this.hasClassName = hasClassName;
	function hasClassName(node , compare ){
		if(!isElement(node) || !isBasicType(compare)){ return false; }
		var classes = node.className;
		if(!isBasicType(classes)) { return false; }
		compare = compare.replace(/\\/gi,"\\");
		
		var reg=new RegExp("(^"+compare+"\\s)|(\\s"+compare+"\\s)|(\\s"+compare+"$)|(^"+compare+"$)","gi");
		
		return classes.match(reg) != null;
	} 

    function stringToArray( stringValue , splitChar ){
		if(isArray(stringValue)){ return stringValue; }
		if(!isBasicType(stringValue)){ return [ stringValue ];}
		var strings = trim(stringValue+'').split(splitChar);
		var rtnArray = [];
		for(var i = 0 ; i < strings.length ; i = i + 1 ){
			var str = trim(strings[i]);
			if (isBasicType(str) && str != "undefined") {
				rtnArray.push(str);
			}
		}
		return rtnArray;
	}

    this.escapeRegExp = escapeRegExp;
    function escapeRegExp(string){
        return string.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
    } 

})();
    
InputUtility['lengthLimit'] =  new (function () {
    /* 外框的inline css */
    document.write('<style type="text/css">div.lengthLimit {display:inline;} :root div.lengthLimit {display: inline-block;}</style>');

    /* 預設的pattern */
    var default_msg_byte = '{enter}/{limit}(字元)';
    var default_msg_font = '{enter}/{limit}(字)';
    /* 修改預設的pattern */
    this.setDefaultMessage = function (text) { if (text) { default_text = text + ''; } };

    /* 一個全型字佔多少byte*/
    var fullWord_byteLength = window.charCountsByte || 2 ;
    this.setByteLength = function(byteLength){
        if (isNumeric(byteLength) && byteLength > 0) {
            fullWord_byteLength=byteLength;
        }
    };
    
    /* 新增一個物件計算內容長度 */
    this.add = function (elem, maxLength, countByByte, customMsg, input_size) {
        if (!InputUtility.utility.isElement(elem)) {
            elem = document.getElementById(elem);
            if (!elem) {
                alert("找不到 id 為 [ " + elem + " ] 物件");
                return;
            }
        }

        var isTextarea = elem.tagName == 'TEXTAREA';

        if (!(
            isTextarea
            || (elem.tagName == 'INPUT' && (elem.type + "").toUpperCase() == 'TEXT')
        )) {
            alert(" 物件 [ " + elem.id + " ] 非可設定 lengthLimit 的物件 ");
            return;
        }

        if (!InputUtility.utility.isNumeric(maxLength) || maxLength <= 0) {
            alert(" 物件 [ " + elem.id + " ] 無設定 lengthLimit 的 maxLength ");
            return;
        }

        /*外框物件*/
        var outerDiv = document.createElement('div');
        outerDiv.className = 'lengthLimit';

        if (InputUtility.utility.isElement(elem.parentNode)) {
            elem.parentNode.replaceChild(outerDiv, elem);
        }
        outerDiv.appendChild(elem);

        /* 主物件設定屬性 */
        elem.setAttribute('_limit_length', maxLength);
        elem.setAttribute('maxLength', maxLength);

        if (!isTextarea && !elem.getAttribute('size') ) {
            elem.setAttribute('size', InputUtility.utility.isNumeric(input_size) ? input_size : maxLength + 2);
        }
        if (countByByte !== false) {
            elem.setAttribute('_countByByte', 'Y');
        }
        if (InputUtility.utility.isString(customMsg) && customMsg) {
            elem.setAttribute('iu_ll_msg', customMsg);
        }
        InputUtility.utility.setEventObserve(elem, 'keyup', function (e) { var elem = InputUtility.utility.getEventTarget(e) || this; updataText(elem, checkInputLength(elem, false)); });
        InputUtility.utility.setEventObserve(elem, 'blur', function (e) { var elem = InputUtility.utility.getEventTarget(e) || this; updataText(elem, checkInputLength(elem, true)); });

        if (window.Prototype) { //有匯入Prototype
            elem.prototype = Object.extend(elem, {
                doLengthLimit: function (e) { updataText(this, checkInputLength(this, true)); }
            });
        } else {//有匯入jQuery
            try{
                elem.prototype = $.extend(elem, {
                    doLengthLimit: function (e) { updataText(this, checkInputLength(this, true)); }
                });
            }catch(e){
                HTMLElement.prototype.doLengthLimit = function(e) { updataText(this, checkInputLength(this, true)); }
            }
        }

        /*文字區塊*/
        var textDiv = document.createElement('div');
        textDiv.style.padding = '3px';
        textDiv.style.textAlign = 'right';
        textDiv.style.fontSize = '12px';
        outerDiv.appendChild(textDiv);
        
        if(customMsg === false){
            textDiv.style.display = 'none';
        }else{
            if (Math.abs(textDiv.offsetWidth - elem.offsetWidth) > 6) {
                textDiv.style.width = elem.offsetWidth + 'px';
            }
            updataText(elem, checkInputLength(elem, true));
        }
        return outerDiv;
    };

    this.doLengthLimit = function( elem ){
        if (!InputUtility.utility.isElement(elem)) {
            elem = document.getElementById(elem);
            if (!elem) {
                return;
            }
        }
        updataText(elem, checkInputLength(elem, true));
    };

    /* 更新文字區塊 */
    function updataText(elem, enter) {
        var textElement = elem.nextSibling;
        var limit = elem.getAttribute('_limit_length');
        var isByByte = !!elem.getAttribute('_countByByte');
        var display_text = InputUtility.utility.hasAttribute(elem,'iu_ll_msg') ? elem.getAttribute('iu_ll_msg') : (isByByte ? default_msg_byte : default_msg_font);
        display_text = display_text.replace(/{limit}/g, ' ' + limit + ' ').replace(/{enter}/g, ' ' + enter + ' ').replace(/{left}/g, ' ' + (limit - enter) + ' ');
        textElement.innerHTML = display_text;
        textElement.style.color = (limit < enter) ? 'red' : '';
    };

    /* 計算長度及截斷長度 */
    var halfWordRegExp = new RegExp('[\x00-\xff]');
    var halfWordRegExp_global = new RegExp('[\x00-\xff]', 'g');
    function checkInputLength(elem, del_str) {

        var limit_length = elem.getAttribute('_limit_length');
        if (!InputUtility.utility.isNumeric(limit_length) && limit_length > 0) { return; }
        limit_length = parseInt(limit_length, 10);

        var text = elem.value + '';

        var org_text_length = text.length;

        if (org_text_length == 0) { return 0; }

        //console.log('input:' + text);

        /* 以單一字元計算 */
        if (!InputUtility.utility.hasAttribute(elem,'_countByByte')) {
            if (del_str && org_text_length > limit_length) {
                text = text.substring(0, limit_length);
                if (confirm("您目前輸入的字串長度已經超出欄位限制長度" + limit_length + "字元，\n您是同意將輸入內容擷取為：\n\n" + text + "\n\n按下 [確定] 同意截取，或按 [取消] 返回修正。")) {
                    elem.value = text;
                } else {
                    elem.focus();
                }
            }
            return text.length;
        }

        var halfWordLength = -1;
        var result = null;
        /*計算半型總數*/
        do {
            result = halfWordRegExp_global.exec(text);
            halfWordLength++;
        } while (result != null);

        /*計算全型總數*/
        var fullWordLength = org_text_length - halfWordLength;

        var stringLength = ( fullWordLength * fullWord_byteLength ) + halfWordLength;

        if (!del_str || stringLength <= limit_length) {
            return stringLength;
        }

        var comfirm_position = org_text_length - 1;
        while (stringLength > limit_length) {
            var test_text = text.charAt(comfirm_position);
            if (halfWordRegExp.test(test_text)) {
                stringLength -= 1;
                //console.log('半形字元:' + stringLength + "(" + test_text);
            } else {
                stringLength -= fullWord_byteLength;
                //console.log('全形字元:' + stringLength + "(" + test_text);
            }
            comfirm_position--;
        }

        text = text.substring(0, (comfirm_position + 1));

        if (confirm("您目前輸入的字串長度已經超出欄位限制長度" + limit_length + "字，\n您同意將輸入內容擷取為：\n\n" + text + "\n\n按下 [確定] 同意截取，或按 [取消] 返回修正。")) {
            elem.value = text;
            //console.log('半形字元:' + halfWordLength + ' ,全形字元:' + fullWordLength + ' ,長度:' + stringLength + '(' + text);
            return stringLength;
        } else {
            elem.focus();
            return (fullWordLength * fullWord_byteLength) + halfWordLength;
        }


    };
})();
    
InputUtility['autoSizingTextarea'] = new (function () {
    
    this.add = function (elem , config ) {
        if (!InputUtility.utility.isElement(elem)) {
            elem = document.getElementById(elem);
            if (!elem) {
                alert("找不到 id 為 [ " + elem + " ] 物件");
                return;
            }
        }
        if (!elem.tagName == 'TEXTAREA') {
            alert("物件 [ " + elem + " ] 非TEXTAREA物件，不得使用autoSizingTextarea");
            return;
        }
        
        var min_rows = elem.getAttribute('rows');
        if(!InputUtility.utility.isNumeric(min_rows)){
            min_rows = config?config.rows:2;
        }
        if(!InputUtility.utility.isNumeric(min_rows)){
            min_rows = 2;
        }
        elem.setAttribute('rows',min_rows);
        elem.setAttribute('minRows',min_rows);
        
        InputUtility.utility.setEventObserve( elem , 'keyup' , textareaKeyUp);
        InputUtility.utility.setEventObserve( elem , 'blur' , rowsReduce);
        
         rowsReduce.call(elem);
    };
    
    this.doAutoSize = function( elem ){
        if (!InputUtility.utility.isElement(elem)) {
            elem = document.getElementById(elem);
            if (!elem) {
                return;
            }
        }
        rowsReduce.call(elem);
    };
    
    function textareaKeyUp(e) {
        if (!e || !e.keyCode) { return; }
        var keyCode = parseInt(e.keyCode);
        if (keyCode == 86 || keyCode == 13) {
            rowsExtend.call(this, e);
        } else if (keyCode == 8 || keyCode == 46 || keyCode == 88) {
            rowsReduce.call(this, e);
        }
    };

    function rowsExtend(e) {
        while (this.scrollHeight > this.offsetHeight) {
            this.setAttribute('rows', ( parseInt(this.getAttribute('rows') || 1) || 1 ) + 1);
        }
    };

    function rowsReduce(e) {
        
        var html = this.value;
        var minHeight = this.getAttribute('minRows');
        if(!InputUtility.utility.isNumeric(minHeight)){
            minHeight = 2;
        }else{
            minHeight = parseInt(minHeight);
        }
        
        var nowLines = ( this.value || '').match(/\n/g);
        nowLines = nowLines ? nowLines.length : 0;

        if( minHeight > nowLines ){
            this.setAttribute('rows', minHeight);
        }else{
            this.setAttribute('rows', nowLines);
        }
        
        if(this.scrollHeight > this.offsetHeight) {
            rowsExtend.call(this, e);
        }
    };

})();



InputUtility['selectWithDatalist'] = new (function () {

    var default_setting = {
        count_info : '{count}/{total}選項',
        placehodlder : '請選擇或輸入',
        timeOut : 300
    }

    
    this.add = function (elem , config ) {
        if (!InputUtility.utility.isElement(elem)) {
            elem = document.getElementById(elem);
            if (!elem) {
                alert("找不到 id 為 [ " + elem + " ] 物件");
                return;
            }
        }

        if(InputUtility.utility.hasClassName(elem,'selectWithDatalist_main')){
            if(elem.previousSibling && InputUtility.utility.hasClassName(elem.previousSibling,'selectWithDatalist')){
                elem.parentNode.removeChild(elem.previousSibling);
            }
        }

        if(!config){ config = {};}
        var defaultSelectValue = config.value;
        var isSelect = elem.tagName == 'SELECT';
        var options = {};
        var hasConfigOptions = InputUtility.utility.isPlainObject(config.options) || InputUtility.utility.isArray(config.options);
        if( isSelect ){
            if( elem.getElementsByTagName('option').length ){
            /* 取得原本select的options */
                var optgroup = elem.getElementsByTagName('optgroup');
                if (optgroup) {
                    for (var i = 0; i < optgroup.length; i++) {
                        var label = optgroup[i].getAttribute('label') || ('_' + i);
                        var opts = optgroup[i].getElementsByTagName('option');
                        var opts_array = [];
                        for (var j = 0; j < opts.length; j++) {
                            opts_array.push({
                                value: opts[j].value,
                                display: opts[j].innerHTML,
                                memo: opts[j].getAttribute('title')
                            });
                            if(!defaultSelectValue && opts[j].selected){
                                defaultSelectValue = opts[j].value;
                            }
                        }
                        options[label] = opts_array;

                    }
                } else {
                    var opts = elem.getElementsByTagName('option');
                    var opts_array = [];
                    for (var j = 0; j < opts.length; j++) {
                        opts_array.push({
                            value: opts[j].value || opts[j].innerHTML,
                            display: opts[j].innerHTML,
                            memo: opts[j].getAttribute('title')
                        });
                        if(!defaultSelectValue && opts[j].selected){
                            defaultSelectValue = opts[j].value;
                        }
                    }
                    options['_'] = opts_array;
                }
            }
            //elem.style.display = 'none';
        }else if(elem.tagName == 'INPUT'){
            if(!hasConfigOptions){
                alert("物件 [ " + elem + " ] 型態為INPUT時，必須傳入options");
                return;
            }
            //elem.setAttribute('type','hidden');
        }else{
             alert("物件 [ " + elem + " ] 型態需為SELECT或INPUT才能使用selectWithDatalist");
        }

        if(hasConfigOptions){
            var valueRender = config.valueKey || 'value';
            var displayRender = config.displayKey || 'display';
            var memoRender = config.memoKey  || 'memo';
            var valueRenderIsFunction = InputUtility.utility.isFunction(valueRender);
            var displayRenderIsFunction = InputUtility.utility.isFunction(displayRender);
            var memoRenderIsFunction = InputUtility.utility.isFunction(memoRender);
            if (InputUtility.utility.isArray(config.options)) {
                var opts_array = options['_'] || [];
                for (var j = 0; j < config.options.length; j++) {
                    var temp = config.options[j];
                    if (InputUtility.utility.isBasicType(temp)) {
                        opts_array.push({
                            value: temp,
                            display: temp
                        });
                    } else if (InputUtility.utility.isPlainObject(temp)) {
                        opts_array.push({
                            value: valueRenderIsFunction ? valueRender(temp) : temp['' + valueRender],
                            display: displayRenderIsFunction ? displayRender(temp) : temp['' + displayRender],
                            memo: memoRenderIsFunction ? memoRender(temp) : temp['' + memoRender]
                        });
                    }
                }
                options['_'] = opts_array;
            } else {
                for (var key in config.options) {
                    var value = config.options[key]
                    if (InputUtility.utility.isBasicType(value)) {
                        if (!options['_']) { options['_'] = [] };
                        options['_'].push({
                            value: key,
                            display: value
                        });
                    } else if (InputUtility.utility.isArray(value)) {
                        var opts_array = [];
                        for (var j = 0; j < value.length; j++) {
                            var temp = value[j];
                            if (InputUtility.utility.isBasicType(temp)) {
                                opts_array.push({
                                    value: temp,
                                    display: temp
                                });
                            } else if (InputUtility.utility.isPlainObject(temp)) {
                                opts_array.push({
                                    value: valueRenderIsFunction ? valueRender(temp) : temp['' + valueRender],
                                    display: displayRenderIsFunction ? displayRender(temp) : temp['' + displayRender],
                                    memo: memoRenderIsFunction ? memoRender(temp) : temp['' + memoRender]
                                });
                            }
                        }
                        options[key] = opts_array;
                    }
                }
            }
        }

        var elem_id = elem.getAttribute('id'); 
        if(!elem_id ){
            elem_id = new Date().getTime();
            elem.setAttribute('id' , elem_id);
        }

        InputUtility.utility.addClassName( elem , 'selectWithDatalist_main');

        var outBlock = document.createElement('div');
        outBlock.className = 'selectWithDatalist';

        var input = document.createElement('input'); 
        input.setAttribute('type','text');
        input.setAttribute('id','selectWithDatalist_'+elem_id);
        input.setAttribute('data-mapping',elem_id);
        input.setAttribute('autocomplete','off');
        InputUtility.utility.addClassName( input , 'selectWithDatalist_input');
        InputUtility.utility.addClassName( input , 'tbBox2');
        /*
        var maxLength = elem.getAttribute('maxLength');
        if(!InputUtility.utility.isNumeric(maxLength)){
            maxLength = config.maxLength;
        }
        if(InputUtility.utility.isNumeric(maxLength)){
            input.setAttribute('maxLength',maxLength);
        }
        */
        outBlock.appendChild(input);
        
        var optionsBlock = document.createElement('div');
        optionsBlock.className = 'selectWithDatalist_options';


        var optionsBlock_scroll = document.createElement('div');
        optionsBlock_scroll.className = 'selectWithDatalist_options_scroll';

        optionsBlock.appendChild(optionsBlock_scroll);

        var infoBar = document.createElement('div');
        var infoBar_pattern = config.info_pattern || default_setting.count_info;
        infoBar_pattern = infoBar_pattern;
        infoBar.className = 'selectWithDatalist_infoBar';
        infoBar.setAttribute('data-pattern',infoBar_pattern);
        optionsBlock.appendChild(infoBar);

        
        outBlock.appendChild(optionsBlock);

        elem.parentNode.insertBefore(outBlock , elem);
        
        resetOptions( optionsBlock , options );
        

        InputUtility.utility.setEventObserve( optionsBlock , 'mouseenter' , option_moveIn);
        InputUtility.utility.setEventObserve( optionsBlock , 'mouseleave' , option_moveOut);
        InputUtility.utility.setEventObserve( input , 'keydown' , input_keyDown);
        InputUtility.utility.setEventObserve( input , 'keyup' , input_keyUp);
        InputUtility.utility.setEventObserve( input , 'click' , input_focus);
        InputUtility.utility.setEventObserve( input , 'focus' , input_focus);
        InputUtility.utility.setEventObserve( input , 'blur' , input_blur);


        var downMark = document.createElement('span');
        downMark.innerHTML = '\u25BC';
        downMark.className = 'selectWithDatalist_downMark';
        downMark.setAttribute("title","顯示選單");
        outBlock.appendChild(downMark);
        InputUtility.utility.setEventObserve( downMark , 'click' , function(e){
            input.focus();
            input_focus.call(input , {target: input});
        });
        
        var clearMark = document.createElement('span');
        clearMark.innerHTML = '\u00D7';
        clearMark.className = 'selectWithDatalist_clearMark';
        clearMark.setAttribute("title","清除輸入");
        outBlock.appendChild(clearMark);
        InputUtility.utility.setEventObserve( clearMark , 'click' , clickShowAll);

        setValue(elem , defaultSelectValue );

        if(window.Prototype){
            input.prototype = Object.extend(elem,{
                setValue:function(value){ setValue(this,value); },
                getValue:function(){ return getValue(this); },
            	getDisplay:function(){ return getDisplay(this); }
            });
        }
        
        /* 2018-6-15
         	ie11 會在form中有一個password欄位時自動開啟autoComplete而且無法關閉，
        	故若該元件包含在form中且該form有一個pw物件
        	便強行增加兩個隱藏pw元件(怕該pw元件被移除)，
        	讓autoComplete失效
    	*/
        var form = elem;
        while( form && form.tagName != 'FORM' && form.tagName != 'BODY'  ){
        	form = form.parentNode;
        }
        if(form && form.tagName == 'FORM'){
        	var pwCount = 0;
        	var inputs = form.getElementsByTagName('INPUT');
        	for(var l = 0 ; l < inputs.length ; l++){
        		if((inputs[l].getAttribute('type')||'').match(/^password$/i)){
        			pwCount++;
        			if(pwCount > 1){ break;}
        		}
        	}
        	if(pwCount == 1){
        		var newPw = document.createElement('input');
        		newPw.setAttribute('type', 'password');
        		newPw.style.display = 'none';
        		form.appendChild(newPw);
        		newPw = document.createElement('input');
        		newPw.setAttribute('type', 'password');
        		newPw.style.display = 'none';
        		form.appendChild(newPw);
        	}
        }

    };

    this.setValue = setValue;
    function setValue( input , value ){
        var show_input;
        if(InputUtility.utility.hasClassName(input , 'selectWithDatalist_main')){
            show_input = document.getElementById('selectWithDatalist_'+input.getAttribute('id'));
        }else if (InputUtility.utility.hasClassName(input , 'selectWithDatalist_input')){
            show_input = input;
            input = document.getElementById(input.getAttribute('data-mapping'));
        }else{
            return;
        }
        if(value !== undefined && value !== null && value != ""){
            value = value + ""; 
            var items = show_input.nextSibling.getElementsByTagName('li');
            for(var i = 0 ; i < items.length ; i ++){
                var item = items[i];
                if(!InputUtility.utility.hasClassName(item,'selectWithDatalist_options_item')){ continue; }
                if(item.getAttribute('data-value') == value ){
                    doSelect( item , true );
                    return;
                }
            }
        }
        InputUtility.utility.addClassName(show_input,'empty_input');
        input.value = '';
        show_input.setAttribute('data-value','');
        show_input.setAttribute('data-prevvalue','');
        show_input.setAttribute('data-iskeyin','N');
        show_input.value = default_setting.placehodlder;
    }

    this.getValue = getValue;
    function getValue( input , value ){
        var show_input;
        if(InputUtility.utility.hasClassName(input , 'selectWithDatalist_main')){
            show_input = document.getElementById('selectWithDatalist_'+input.getAttribute('id'));
        }else if (InputUtility.utility.hasClassName(input , 'selectWithDatalist_input')){
            show_input = input;
            input = document.getElementById(input.getAttribute('data-mapping'));
        }else{
            return;
        }
        return input.value;
    }

    this.getDisplay = getDisplay;
    function getDisplay( input , value ){
        var show_input;
        if(InputUtility.utility.hasClassName(input , 'selectWithDatalist_main')){
            show_input = document.getElementById('selectWithDatalist_'+input.getAttribute('id'));
        }else if (InputUtility.utility.hasClassName(input , 'selectWithDatalist_input')){
            show_input = input;
            input = document.getElementById(input.getAttribute('data-mapping'));
        }else{
            return;
        }
        return show_input.value;
    }

    function resetOptions( selectWithDatalist_options , options){
        if(!InputUtility.utility.hasClassName(selectWithDatalist_options,'selectWithDatalist_options')){ return;}
        var optionsBlock_scroll = selectWithDatalist_options.firstChild;
        optionsBlock_scroll.innerHTML = '';

        var org_input = document.getElementById(selectWithDatalist_options.previousSibling.getAttribute('data-mapping'));
        var isSelect = org_input.tagName == 'SELECT';
        if(isSelect){
            org_input.innerHTML='';
        }

        var maxLength = 0;

        var options_count = 0;
        for(var label in options){
            var opts = options[label]; 
            if(!opts.length){ continue; }
            var ul = document.createElement('ul');
            if(label.match(/^[^_]/)){
                var li = document.createElement('li');
                li.className = 'selectWithDatalist_options_label';
                li.innerHTML = label;
                ul.appendChild(li); 
            }
            for(var i = 0 ; i < opts.length ; i++ ){
                var li = document.createElement('li');
                li.className = 'selectWithDatalist_options_item';
                li.setAttribute('optgroup',label);
                li.setAttribute('index',i);
                li.setAttribute('data-value',opts[i].value);
                InputUtility.utility.setEventObserve(li, 'click', clickItem )

                var span = document.createElement('span');
                span.className = 'selectWithDatalist_options_item_display';
                span.innerHTML = opts[i].display;
                li.appendChild(span);

                if(opts[i].memo){
                    span = document.createElement('span');
                    span.className = 'selectWithDatalist_options_item_memo';
                    span.innerHTML = opts[i].memo;
                    li.appendChild(span);
                }

                if(isSelect){
                    var option = document.createElement('option');
                    option.value = opts[i].value;
                    option.innerHTML = opts[i].display;
                    org_input.appendChild(option);
                }

                maxLength = Math.max( maxLength , opts[i].display.length);
                
                ul.appendChild(li); 

                options_count++;
            }
            optionsBlock_scroll.appendChild(ul);
        }

        selectWithDatalist_options.previousSibling.setAttribute('maxLength' , maxLength);

        var infoBar = optionsBlock_scroll.nextSibling;
        infoBar.setAttribute('data-count',options_count);
        updateInfoBar(infoBar,options_count);

        InputUtility.utility.addClassName(selectWithDatalist_options,'lengthCount');
        var width = selectWithDatalist_options.offsetWidth;
        selectWithDatalist_options.style.width = width + 'px';
        selectWithDatalist_options.previousSibling.style.width = width + 'px';
        InputUtility.utility.removeClassName(selectWithDatalist_options,'lengthCount');
        
    }


    function clickItem(e){
        cancelEventBubble(e);
        var target = InputUtility.utility.getEventTarget(e)||this;
        //InputUtility.utility.removeClassName(target.nextSibling,'waitToClose');
        doSelect(target);

    }

    function doSelect( node , doNotFocus ){
        if(!InputUtility.utility.isElement(node)){ return; }

        while(node && !InputUtility.utility.hasClassName(node,'selectWithDatalist_options_item')){ 
            node = node.parentNode;
        }
        if(!node){ return; }
        var displayValue = node.firstChild.innerHTML;
        var value = node.getAttribute('data-value');

        var selectWithDatalist_options = node;
        while(selectWithDatalist_options && !InputUtility.utility.hasClassName(selectWithDatalist_options , 'selectWithDatalist_options')){
            selectWithDatalist_options = selectWithDatalist_options.parentNode;
        }
        if(!selectWithDatalist_options){ return; }

        var selectWithDatalist_input = selectWithDatalist_options.previousSibling;
        var org_input = document.getElementById(selectWithDatalist_input.getAttribute('data-mapping'));
        var org_input_value = org_input.value;
        selectWithDatalist_input.value = displayValue;
        selectWithDatalist_input.setAttribute('data-value',value);
        selectWithDatalist_input.setAttribute('data-prevvalue','');
        selectWithDatalist_input.setAttribute('data-iskeyin','N');
        InputUtility.utility.removeClassName(selectWithDatalist_input,'empty_input');
        org_input.value = value;

        if(doNotFocus == true ){ return; }

        selectWithDatalist_input.focus();
        input_focus({target:selectWithDatalist_input});
        var selectWithDatalist_options = selectWithDatalist_input.nextSibling;
        //closeOptions(selectWithDatalist_options,true);
        setTimeout(function(){
            InputUtility.utility.removeClassName(selectWithDatalist_options,'inputFocus');
            InputUtility.utility.removeClassName(selectWithDatalist_options,'optionsIn');
            InputUtility.utility.removeClassName(selectWithDatalist_options.parentNode,'working');
            InputUtility.utility.fireEventObserving( org_input , 'blur');
            if(org_input_value != org_input.value){
                InputUtility.utility.fireEventObserving( org_input , 'change');
            }
        }, default_setting.timeOut);
    }

    function updateInfoBar( infoBar , length ){
        if(!InputUtility.utility.isElement(infoBar)){return;}
        var pattern = infoBar.getAttribute('data-pattern');
        if(!pattern){return;}
        infoBar.innerHTML = pattern.replace(/{total}/g,infoBar.getAttribute('data-count')).replace(/{count}/g,length||0);
    }

    
    function clickShowAll(e){
        var target = InputUtility.utility.getEventTarget(e)||this;
        var node = target.parentNode;
        if(node && !InputUtility.utility.hasClassName(node,'selectWithDatalist')){ 
            return;
        }
        node = node.firstChild;
        node.value = '';
        node.focus();
        input_focus({target:node});
        //updateOptions(node);
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

        var target = InputUtility.utility.getEventTarget(e) || this;

        if(!InputUtility.utility.hasClassName(target.nextSibling,'inputFocus')){
            InputUtility.utility.addClassName(target.nextSibling,'inputFocus');
            InputUtility.utility.addClassName(target.parentNode,'working');
        }        
        if(keynum != 9 && keynum != 13 && keynum != 38 && keynum != 40){ return; }

        cancelEventBubble(e);

        var options = target.nextSibling.getElementsByTagName('li');
        var active_i = -1;
        var availableOptions = [];
        for(var i = 0; i < options.length ; i++ ){
            if(!InputUtility.utility.hasClassName(options[i],'selectWithDatalist_options_item')){
                continue;
            }
            if(InputUtility.utility.hasClassName(options[i],'deactivate')){
                continue;
            }
            availableOptions.push(options[i]);
            if(InputUtility.utility.hasClassName(options[i],'active')){
                active_i  = availableOptions.length-1;
            }
        }
        
        if(active_i >= 0 ){
            InputUtility.utility.removeClassName(availableOptions[active_i],'active');
        }
        var newActive = -1;
		switch(keynum){
            case 9: //TAB
				InputUtility.utility.removeClassName(target.nextSibling,'inputFocus');
                closeOptions(target.nextSibling);
				cancelEventBubble(e);
				break;
            case 13: //ENTER
				if(active_i >= 0){
					doSelect(availableOptions[active_i]);
				}
                return;
			case 38: //UP
                if(active_i>0){
                    newActive = active_i-1;
                }
				break;
			case 40: //DOWN
				if(active_i+1 < availableOptions.length ){
                    newActive = active_i+1;
                }
				break;
		}
        if(newActive >= 0 ){
            InputUtility.utility.addClassName(availableOptions[newActive],'active');
            var position_y = availableOptions[newActive].offsetTop;
            var scrollDiv = target.nextSibling.firstChild;
            if(scrollDiv.scrollTop > position_y){
                scrollDiv.scrollTop = position_y;
            }else if( ( position_y + availableOptions[newActive].offsetHeight) > scrollDiv.scrollTop+scrollDiv.offsetHeight){
                scrollDiv.scrollTop = position_y-availableOptions[newActive].offsetHeight;
            }
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

        var target_input = InputUtility.utility.getEventTarget(e);

        switch(keynum){
			case 27: //ESC
                InputUtility.utility.removeClassName(target_input.nextSibling,'inputFocus');
                closeOptions(target_input.nextSibling);
				cancelEventBubble(e);
				return;
			//case 13: //ENTER : 開放此動作，因為輸入法對於ENTER時會修改內容
			case 38: //UP
			case 40: //DOWN
				cancelEventBubble(e);
				return;
			case 37: //left
			case 39: //right
			case 35: //HOME
			case 36: //END
				return;	
			default:
                //console.log("press key :" + keynum);
		}
        updateOptions( target_input || this );
        
    }

    function input_focus(e){
        var target = InputUtility.utility.getEventTarget(e)||this;
        if(InputUtility.utility.hasClassName(target.nextSibling,'inputFocus')){ return; }
        target.nextSibling.firstChild.scrollTop = 0;
        if(InputUtility.utility.hasClassName(target,'empty_input')){
            InputUtility.utility.removeClassName(target,'empty_input');
            target.setAttribute('data-value','');
            target.setAttribute('data-prevvalue','');
            target.value = "";
        }
        updateOptions(target);
        InputUtility.utility.addClassName(target.nextSibling,'inputFocus');
        InputUtility.utility.addClassName(target.parentNode,'working');
    }

    function input_blur(e){
        var target = InputUtility.utility.getEventTarget(e)||this;
        if(!InputUtility.utility.hasClassName(target.nextSibling,'inputFocus')){ return; }
        InputUtility.utility.removeClassName(target.nextSibling,'inputFocus');
        
        if(InputUtility.utility.hasClassName(target.nextSibling,'optionsIn')){ return; }
        setTimeout(function(){
            closeOptions(target.nextSibling,null,e);
        } , default_setting.timeOut);
    }

    function option_moveIn(e){
        var target = InputUtility.utility.getEventTarget(e)||this;
        while(target && !InputUtility.utility.hasClassName(target,'selectWithDatalist_options')){
            target = target.parentNode;
        }
        if(!target){
            return;
        }
        InputUtility.utility.addClassName(target,'optionsIn');
        //InputUtility.utility.addClassName(target.parentNode,'working');
    }

    function option_moveOut(e){
        var target = InputUtility.utility.getEventTarget(e)||this;
        while(target && !InputUtility.utility.hasClassName(target,'selectWithDatalist_options')){
            target = target.parentNode;
        }
        if(!target){
            return;
        }
        setTimeout(function(){
            InputUtility.utility.removeClassName(target,'optionsIn');
            closeOptions(target,null,e);
        } , default_setting.timeOut);
    }

    function closeOptions(selectWithDatalist_options, closeWithNoChecked, logEvent){
        
        if(!InputUtility.utility.hasClassName(selectWithDatalist_options,'selectWithDatalist_options')){ return;}
        //仍然在焦點，不執行關閉
        if( InputUtility.utility.hasClassName(selectWithDatalist_options,'inputFocus') 
            || InputUtility.utility.hasClassName(selectWithDatalist_options,'optionsIn') ){
                return;
        }
        InputUtility.utility.removeClassName(selectWithDatalist_options,'inputFocus');
        InputUtility.utility.removeClassName(selectWithDatalist_options,'optionsIn');
        InputUtility.utility.removeClassName(selectWithDatalist_options.parentNode,'working');

        var selectWithDatalist_input = selectWithDatalist_options.previousSibling;
        //表示資料沒有被人為變更過，不進行重新設定資料
        if(selectWithDatalist_input.getAttribute('data-iskeyin') == 'N'){
            return;
        }

        var hasFound = false;
        var onlyOneOption = null;
        var input_value = InputUtility.utility.escapeRegExp(selectWithDatalist_input.value);
        var testInputValueExp = new RegExp("^"+input_value+"$"); 
        var options = selectWithDatalist_options.getElementsByTagName('li');
        
        for( var i = 0 ; i < options.length ; i++){
            if(!InputUtility.utility.hasClassName(options[i],'selectWithDatalist_options_item')){
                continue;
            }
            var displayValue = options[i].firstChild.innerHTML 
            var dataValue = options[i].getAttribute('data-value'); 
            if(displayValue.match(testInputValueExp)){
                hasFound = dataValue
                break;
            }
            
            if(onlyOneOption !== false && !InputUtility.utility.hasClassName(options[i],'deactivate')){
                if(onlyOneOption !== null){
                    onlyOneOption = false;
                    continue;
                }
                onlyOneOption = [dataValue ,displayValue];
            }
        }
        
        var org_input = document.getElementById(selectWithDatalist_input.getAttribute('data-mapping'));
        var org_input_value = org_input.value;
        if(hasFound){
            org_input.value = hasFound;
            selectWithDatalist_input.setAttribute('data-value',hasFound);
            selectWithDatalist_input.setAttribute('data-prevvalue','');
            InputUtility.utility.removeClassName(selectWithDatalist_input,'empty_input');
        }else if(onlyOneOption){
            org_input.value = onlyOneOption[0];
            selectWithDatalist_input.value = onlyOneOption[1];
            selectWithDatalist_input.setAttribute('data-value',onlyOneOption[0]);
            selectWithDatalist_input.setAttribute('data-prevvalue','');
            InputUtility.utility.removeClassName(selectWithDatalist_input,'empty_input');
        }else{
            org_input.value = '';
            selectWithDatalist_input.value = default_setting.placehodlder;
            selectWithDatalist_input.setAttribute('data-value','');
            selectWithDatalist_input.setAttribute('data-prevvalue','');
            InputUtility.utility.addClassName(selectWithDatalist_input,'empty_input');
        }
        selectWithDatalist_input.setAttribute('data-iskeyin','N');

        InputUtility.utility.fireEventObserving( org_input , 'blur');
        if(org_input_value != org_input.value){
            InputUtility.utility.fireEventObserving( org_input , 'change');
        }   
    }

    function updateOptions(target_elem){	
        if(!InputUtility.utility.isElement(target_elem) || !InputUtility.utility.hasClassName(target_elem,'selectWithDatalist_input') ){return;}

        var value = InputUtility.utility.trim(target_elem.value||"");
        var prevValue = target_elem.getAttribute('data-prevvalue');
        if(value && value == prevValue){ return; }
        target_elem.setAttribute('data-iskeyin','Y');
        target_elem.setAttribute('data-prevvalue',value);
        var regExp = new RegExp(InputUtility.utility.escapeRegExp(value) , 'i');
        var available = 0;
        var lists = target_elem.nextSibling.getElementsByTagName('ul');
        for(var i = 0 ; i < lists.length ; i++){
            var options = lists[i].getElementsByTagName('li');
            var groupDisplay = false;
            var hasDisplayed = false;
            for(var j = 0 ; j < options.length ; j++){
                 
                 if(j === 0 && InputUtility.utility.hasClassName(options[j],'selectWithDatalist_options_label')){
                     groupDisplay = options[j].innerHTML.match(regExp);
                     continue;
                 }
                 if(!InputUtility.utility.hasClassName(options[j],'selectWithDatalist_options_item')){
                     continue;
                 }
                 var spans = options[j].getElementsByTagName('span');
                 if( spans.length < 1){
                     continue;
                 }
                 InputUtility.utility.removeClassName(options[j],'active');
                 if( groupDisplay || (spans[0].innerHTML+'').match(regExp) ){
                     InputUtility.utility.removeClassName(options[j],'deactivate');
                     hasDisplayed = true;
                     available++;
                 }else{
                     InputUtility.utility.addClassName(options[j],'deactivate');
                 }
             
            }
            if(hasDisplayed){
                InputUtility.utility.removeClassName(lists[i],'deactivate');
            }else{
                InputUtility.utility.addClassName(lists[i],'deactivate');
            }
        }
        
        updateInfoBar(target_elem.nextSibling.lastChild,available);

	}

    function cancelEventBubble(e){ 
		if (!e) 
		  e = window.event; 
		//IE9 & Other Browsers 
		if (e.stopPropagation) { 
		  e.stopPropagation(); 
		} 
		//IE8 and Lower 
		else { 
		  e.cancelBubble = true; 
		} 
	}

})();
