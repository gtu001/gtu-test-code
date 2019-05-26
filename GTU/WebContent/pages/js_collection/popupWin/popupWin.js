if (!window.CSS_locate_base) {
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

if (!window.HTML_locate_base) {
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

/* ¤Þ¤JCSS¨Ï¥Î */
var css_filePath = 'html/CM/css/ui/popupWin.css';
if (window.CSS_locate_base) {
    document.write('<link rel="stylesheet" type="text/css" href="' + CSS_locate_base + css_filePath + '" />');
} else if (window.HTML_locate_base) {
    var PROD_CssBase = HTML_locate_base.replace(/Web\//, 'Docs/');
    document.write('<link rel="stylesheet" type="text/css" href="' + HTML_locate_base + css_filePath + '" />');
    document.write('<link rel="stylesheet" type="text/css" href="' + PROD_CssBase + css_filePath + '" />');
}

function PopupWinUI(){

	var ui_element , ui_block;
    
    var title , title_span, close_btn , main;
    
    var adj_cover, adj_window;
    
    var callbackFunction;
    
    var onCloseFunction;
    
    var closeConfirm;
    
    var configs = {
        'default_title':  '&nbsp;',
        'closeTitle':     'close window',
        'width':          0.9,
        'height':         0.8,
        'top':            0.4,
        'left':           0.5,
        'minWidth':       150,
        'minHeight':      150
        
    };
	
    function setConfig(name,value){
        
        //TODO: isWorking
        
        if(ui_element){
            ui_element.parentNode.removeChild(ui_element);
        }
        
		if(isString(name)){
			configs[name] = value;
		}
	}
	
	function buildUI(){
		
		if(ui_element){ return; }
        
		ui_block =                   createElement('table',null,null,['popupWin-table']);
		var tbody =                  createElement('tbody',null,null,null,null,ui_block);
		
		var tr =                     createElement('tr',null,null,null,null,tbody);
		var resize_top_left =        createElement('td',{ id:'popupWin-resizeTopLeft' },null,['popupWin-resize'],{mousedown:doAdjWindow},tr);
		var resize_top =             createElement('td',{ id:'popupWin-resizeTop' },null,['popupWin-resize'],{mousedown:doAdjWindow},tr);
		var resize_top_right =       createElement('td',{ id:'popupWin-resizeTopRight' },null,['popupWin-resize'],{mousedown:doAdjWindow},tr);
		
		tr =                         createElement('tr',null,null,null,null,tbody);
		var resize_left =            createElement('td',{ id:'popupWin-resizeLeft', rowSpan:2 },null,['popupWin-resize'],{mousedown:doAdjWindow},tr);
        var td =                     createElement('td',null,null,['popupWin-header'],null,tr);
		title =                      createElement('div',{ id:'popupWin-title' },null,null,{mousedown:doAdjWindow},td);
        close_btn =                  createElement('button', {id:'popupWin-close', type:'button',title:configs.closeTitle},null,['popupWin-CloseBtn'],{click:clickClose},title,'&times;');
        title_span =                 createElement('span', null,null,null,null,title,configs.default_title);
		var resize_right  =          createElement('td',{ id:'popupWin-resizeRight', rowSpan:2 },null,['popupWin-resize'],{mousedown:doAdjWindow},tr);
        
        
		
		tr =                         createElement('tr',null,null,null,null,tbody);
        td =                         createElement('td',null,null,null,null,tr);
		main =                       createElement('div',{ id:'popupWin-main' },null,null,null,td);
		
		tr =                         createElement('tr',null,null,null,null,tbody);
		var resize_bottom_left =     createElement('td',{ id:'popupWin-resizeBottomLeft' },null,['popupWin-resize'],{mousedown:doAdjWindow},tr);
		var resize_bottom =          createElement('td',{ id:'popupWin-resizeBottom' },null,['popupWin-resize'],{mousedown:doAdjWindow},tr);
		var resize_bottom_right  =   createElement('td',{ id:'popupWin-resizeBottomRight' },null,['popupWin-resize'],{mousedown:doAdjWindow},tr);
		
		
		ui_element =                 createElement('div',null,null,['popupWin'],null,document.body,ui_block);
        
        adj_cover =                  createElement('div',{id:'popupWin-adj-cover'},null,null,{'mouseup':finishAjdWindow, 'mousemove':moveAjdWindow},ui_element);
        adj_window =                 createElement('div',{id:'popupWin-adj-window'},null,null,{'mouseup':finishAjdWindow, 'mousemove':moveAjdWindow},adj_cover);
	}

    var entity = this;
 
	/**
	 *  @public
	 *	@class §Q¥Î DIV ²£¥Íµøµ¡
	 *	@param config (Map)
	 *	@param config.src (String) : ©Ò­n³sµ²¤§ url¡C
	 *	@param config.width (Number) : ¶}±Òµøµ¡¼e«×¡C
	 *	@param config.height (Number) : ¶}±Òµøµ¡¼e«×¡C
	 *	@param config.left (Number) : ¶}±Òµøµ¡¶ZÂ÷¥ªÃä¤§¼e«×¡C
	 *	@param config.top (Number) : ¶}±Òµøµ¡¶ZÂ÷¤W¤è¤§°ª«×¡C
	 *	@param config.scrolling (String<yes/no>): ¬O§_²£¥Í scroll bar¡C
	 *	@param config.parameters (Array/Map/String) : ¶Ç»¼°Ñ¼Æ¡C
	 *	@param config.closeConfirm (Map)
	 *	@param config.closeConfirm.assignConfirmType (Boolean) : §PÂ_·í¥X²{ confirm ®É¡AÃö³¬ popupWin ªº®É¾÷¬O¦bÂI¿ï¡u½T»{ (true)¡vÁÙ¬O¡u¨ú®ø (false)¡v¡F­Y¤£³]©w«h¹w³]¬°¡u½T»{¡v¡C 
	 *	@param config.closeConfirm.msg (String) : confirm ®É©ÒÅã¥Ü¤§°T®§¤º®e¡C
     *	@param config.title (String) : µøµ¡¼ÐÃD¡C
     *	@param config.resizable (boolean) : ¨Ï¥ÎŽÍ¥i§_½Õ¾ã¤j¤p¡C
     *	@param config.movable (boolean) : ¨Ï¥ÎŽÍ¥i§_²¾°Êµøµ¡¡C
     *	@param config.closeBtn (boolean) : µøµ¡¤W¤è¬O§_Åã¥ÜÃö³¬«ö¶s¡C
     *	@param config.isFullPopup (boolean) : ¬O§_¥þµøµ¡Åã¥Ü¡C
	 **/
    entity.popup = popup;
    function popup( inConfig, inWidth, inHeight, inLeft, inTop, inScrolling, inParameters )
    {
		
		var config;
    	if( isString(inConfig) ) { /* ¦¹¬q³B²z¬O´£¨Ñµ¹°ê»Ú§ë¸ê³¡¥÷¤l¨t²Î¨Ï¥ÎÂÂª©ªº popupWin ¨Ï¥Î */
			config = {
				src: inConfig,
				width: inWidth,
				height: inHeight,
				left: inLeft,
				top: inTop,
				scrolling: inScrolling,
				parameters: inParameters
            };
	    } else if (isObject(inConfig )){
			config = inConfig;
	    } else {
            window.console && window.console.log('[popupWin][popup] inConfig Error; ');
            return;
        }

		var src = config.src || '';
        
        if(!src){ 
            window.console && window.console.log('[popupWin][popup] inConfig without src; ');
            return; 
        }
        
        buildUI();
        
        if(hasClassName(document.body, 'popupWinActive')){
            if(window.console){
                console.log('[popupWin][popup] popupwin already executing, config');
                console.log(config);
            }
            return;
        }
        
        callbackFunction = entity.callbackHandler != callbackFunc ? entity.callbackHandler : ( config.cb || config.onBack );
        
        onCloseFunction = config.onClose || config.onclose;
        
        closeConfirm = config.closeConfirm;
        
        addClassName(document.body,'popupWinActive');
        
        main.innerHTML = ''; /* make sure delete privous page */
        
        ui_element.style.display = 'block';
        

        var formatParam = function( parent , inputParam , default_name ){
						
            if(inputParam === null || inputParam === undefined ){ return parent; }
            
            if( isBasicType(inputParam) ){
                parent[default_name] = inputParam;
            }else if( isArray(inputParam) ){
                for( var i=0; i<inputParam.length; i++){
                    formatParam( parent , inputParam[i] , default_name + '_' + i );
                }
            }else if( isObject(inputParam) ){
                for( var ipn in inputParam ){
                    formatParam( parent , inputParam[ipn] , ipn );
                }
            }
            
            return parent;
        };
        
        var inputParam = formatParam( {} , config['parameters'] , 'parameters' );
        
        if(src.indexOf('?') > 0){
            var position_mark = src.indexOf('?');
            var query_string = src.substring(position_mark+1).split('&');
            src = src.substring(0,position_mark);
            for(var i = 0 ; i < query_string.length ; i++ ){
                position_mark = query_string[i].indexOf('=');
                if(position_mark <= 0 && position_mark != position_mark.length){ continue; }
                var key = query_string[i].substring(0,position_mark);
                var value =  query_string[i].substring(position_mark+1);
                inputParam[key] = value;
            }
        }
        
        var iFrame_id = 'popupwin_'+( new Date().getTime() ); 
        
        var ui_is_absolute = getStyle( ui_element , 'position') == 'absolute';
        
        if(!window.addEventListener && !ui_is_absolute){  //for old IE
            document.body.scroll = 'no';
        }
        
        var isFullPop = getBoolean(config.isFullPopup, false);
        var no_resize = isFullPop || !getBoolean(config.resizable, true);
        var no_move = isFullPop || !getBoolean(config.movable, true);
        var no_closeBtn = !isFullPop && !getBoolean(config.closeBtn, true);
        if(no_resize){
            addClassName(ui_element,'no-resize');
        }else{
            removeClassName(ui_element,'no-resize');
        }
        
        if(no_move){
            addClassName(ui_element,'no-move');
        }else{
            removeClassName(ui_element,'no-move');
        }
        
        if(no_closeBtn){
            addClassName(ui_element,'no-close');
        }else{
            removeClassName(ui_element,'no-close');
        }
        
        if(!window.addEventListener){
            var baseTr = main.parentNode.parentNode;
            if(no_resize){
                baseTr.previousSibling.previousSibling.style.display = 'none';
               baseTr.nextSibling.style.display = 'none';
            }else{
                baseTr.previousSibling.previousSibling.style.display = '';
                baseTr.nextSibling.style.display = '';
            }
            if(isFullPop){
                baseTr.previousSibling.style.display = 'none';
            }else{
                baseTr.previousSibling.style.display = '';
            }
        }
        
        if(isString(config.title)){
            title_span.appendChild(document.createTextNode(config.title));
        }
        
        var newIFrame = createElement('iframe',{id:iFrame_id, name:iFrame_id, frameBorder:0 },null,null,{'load':setCloseFunc},main);
        try{
            newIFrame.contentWindow.location.replace('../../../html/CM/js/ajax/dummy.jsp');
        }catch(e){
            newIFrame.setAttribute('src','../../../html/CM/js/ajax/dummy.jsp');
        }
        //src:'../../../html/CM/js/ajax/dummy.jsp',
        
        /* *** IMPORTANT: This is a BUG FIX for Internet Explorer *** */ 
        if(window.self && self.frames[iFrame_id].name != iFrame_id) { 
            self.frames[iFrame_id].name = iFrame_id; 
        }
        
        if(ui_is_absolute){
            ui_element.style.height = '';
            ui_element.style.width = '';
            ui_element.style.top = '';
        }
        
        var windowWidth = ui_element.offsetWidth;
        var windowHeight = ui_element.offsetHeight;
        
        if(ui_is_absolute ){
            ui_element.style.height = (windowHeight+15) + 'px';
            ui_element.style.width = (windowWidth+15) + 'px';
            ui_element.style.top = document.body.scrollTop + 'px';
            keepScrollAt(document.body.scrollTop);
        }
        
        if(isFullPop){
            addClassName(ui_element,'fullPop');
            newIFrame.style.width = (windowWidth - (ui_block.offsetWidth - newIFrame.offsetWidth)) + 'px';
            newIFrame.style.height = (windowHeight - (ui_block.offsetHeight - newIFrame.offsetHeight)) + 'px';
            ui_block.style.top = '0px';
            ui_block.style.left = '0px';
        }else{
            removeClassName(ui_element,'fullPop');
            var temp_px = getPX(config.width , configs.width , windowWidth , 1 );
            newIFrame.style.width = (temp_px[0] - (ui_block.offsetWidth - newIFrame.offsetWidth)) + 'px';
            if(temp_px[1]){
                newIFrame.setAttribute('width_Ratio',temp_px[1]);
            }
            temp_px = getPX(config.height , configs.height , windowHeight , 1 );
            newIFrame.style.height = (temp_px[0] - (ui_block.offsetHeight - newIFrame.offsetHeight)) + 'px';
            if(temp_px[1]){
                newIFrame.setAttribute('height_Ratio',temp_px[1]);
            }
            
            var config_top = getPX(config.top , configs.top , windowHeight - ui_block.offsetHeight , 1)[0];
            if(config_top > (( parseInt(document.body.scrollTop) || 0 ) + windowHeight )){
                config_top = config_top - document.body.scrollTop;
            }
            config_top = Math.max(config_top , 0);
            
            var config_left = getPX(config.left , configs.left , windowWidth - ui_block.offsetWidth , 1)[0];
            if(config_left > (( parseInt(document.body.scrollLeft) || 0) + windowWidth )){
                config_left = config_left - document.body.scrollLeft;
            }
            config_left = Math.max(config_left , 0);
            
            ui_block.style.top =  config_top + 'px';
            ui_block.style.left = config_left + 'px';
        }
        
        
        var scrolling = getBoolean(config.scrolling,'Y');
        if(!window.addEventListener){  //for old IE
            if(scrolling){
                newIFrame.setAttribute('scrolling','auto');
            }else{
                newIFrame.setAttribute('scrolling','no');
            }
        }else{
            if(scrolling){
                newIFrame.style.overflow= 'srcoll';
                newIFrame.style.overflowY= 'srcoll';
                newIFrame.style.overflowX= 'auto';
            }else{
                newIFrame.style.overflow= 'hidden';
                newIFrame.style.overflowY= 'hidden';
                newIFrame.style.overflowX= 'hidden';
            }
        }
        
        var submitForm = createElement('form',{action:src, target:iFrame_id, method:'post'});
        for(var key in inputParam){
            createElement('input',{type:'text', value:inputParam[key], name:key , submitName:key} ,null,null,null,submitForm);
        }
        
        try{
			var eBAF_loginSystemInfo = top['eBAF_loginSystemInfo'];
			var eBAF_UserObject_Flag = top['eBAF_UserObject_Flag'];
			if(eBAF_loginSystemInfo){
                createElement('input',{type:'text', value:eBAF_loginSystemInfo, name:'eBAF_loginSystemInfo' , submitName:'eBAF_loginSystemInfo'} ,null,null,null,submitForm);
			}
			if(eBAF_UserObject_Flag){
                createElement('input',{type:'text', value:eBAF_UserObject_Flag, name:'eBAF_UserObject_Flag' , submitName:'eBAF_UserObject_Flag'} ,null,null,null,submitForm);
			}
		}catch(e){
		}
        
        main.appendChild(submitForm);
        
        if(isFunction(window.submitOnce)){
            window.submitOnce(submitForm);
        }else{
            submitForm.submit();
        }
        
        main.removeChild(submitForm);
        
        newIFrame.focus();    
        
    }
    
    function keepScrollAt(top_val){
        if(!hasClassName(document.body,'popupWinActive')){ return; }
        if(document.body.scrollTop != top_val){
            document.body.scrollTop = top_val;
        }
        setTimeout(function(){ keepScrollAt(top_val); } , 1000);
    }
    
    function setCloseFunc(e){
        
        var target = getEventTarget(e);
        
        if(target.tagName == 'IFRAME'){
            var contentWindow = target.contentWindow;
            if(!contentWindow){
                contentWindow = target.document;
                if(!contentWindow){
                    return;
                }
                contentWindow = contentWindow.window;
            }
        }else{
            window.console && console.log('[popupWin][setCloseFunc] rewrite Close Function Error: target not found ')
            return;
        }
        
        try{
            contentWindow.isPopupWin = true;
            contentWindow.close = closeFunc;
            contentWindow.popupWinclose = closeFunc;
            contentWindow.popupWinBack = callbackAndCloseFunc;
            if(isFunction(callbackFunction)){
                contentWindow.callbackHandler = callbackFunc;
                contentWindow.callbackProxy = callbackFunc;
            }
            contentWindow.focus();
        }catch(e){
            window.console && console.log('[popupWin][setCloseFunc] rewrite Close Function Error: '+e);
        }
        
    }
    
    	/**
	 *  @public
	 *	@class §Q¥Î DIV ²£¥Íµøµ¡¡]»P¥Ø«e©ÒÂsÄý¤§­¶­±¤j¤p¬Û¦P¡^
	 *	@param config (Map)
	 *	@param config.src (String) : ©Ò­n³sµ²¤§ url¡C
	 *	@param config.scrolling (String<yes/no>): ¬O§_²£¥Í scroll bar¡C
	 *	@param config.parameters (Array/Map/String) : ¶Ç»¼°Ñ¼Æ¡C
	 **/
    entity.fullPopup = function( inConfig, inParameters )
    {
		
		var config;
    	if( isString(inConfig) ) { /* ¦¹¬q³B²z¬O´£¨Ñµ¹°ê»Ú§ë¸ê³¡¥÷¤l¨t²Î¨Ï¥ÎÂÂª©ªº popupWin ¨Ï¥Î */
			config = {
				src: inConfig,
				parameters: inParameters
            };
	    } else if (isObject(inConfig )){
			config = inConfig;
	    } else {
            window.console && window.console.log('[popupWin][fullPopup] inConfig Error; ');
            return;
        }

		var src = config.src || '';
        
        if(!src){ 
            window.console && window.console.log('[popupWin][fullPopup] inConfig without src; ');
            return; 
        }
        
        config.isFullPopup = true;
        config.closeConfirm = null;
        popup(config);
        
    }
    
    function clickClose(e){
        if(isObject(closeConfirm) && isString(closeConfirm.msg)){
            var assignConfirmType = closeConfirm['assignConfirmType']===false? false:true;
            if(confirm(closeConfirm['msg']) !== assignConfirmType){
                return;
            }
        }
        closeFunc(e);
        cancelEventBubble(e);
    }
    
    
    entity.close = closeFunc;
    function closeFunc(){
        
        if(!hasClassName(document.body,'popupWinActive')){
            window.close();
            return;
        }
	   
        if(isFunction(onCloseFunction)){onCloseFunction()};
        
        window.focus();

	    title_span.innerHTML = configs.default_title;
        main.innerHTML = '';
        ui_element.style.display = 'none';
        
        onCloseFunction = null;
        callbackFunction = null;
        removeClassName(document.body,'popupWinActive');
        if(!window.addEventListener){ //for old IE
            ui_element.style.height = '';
            ui_element.style.width = '';
            ui_element.style.top = '';
            document.body.scroll = '';
        }
        
    }
    
    entity.back = callbackAndCloseFunc;
    function callbackAndCloseFunc(obj1,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9,obj0){
        callbackFunc(obj1,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9,obj0);
        closeFunc();
    }
    
    entity.callbackHandler = callbackFunc;
    entity.callbackProxy = callbackFunc;
    function callbackFunc(obj1,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9,obj0){
    	try {
        	if(isFunction(callbackFunction)){
        	    callbackFunction.call(
	                this,
	                doToJSON(obj1),
	                doToJSON(obj2),
	                doToJSON(obj3),
	                doToJSON(obj4),
	                doToJSON(obj5),
	                doToJSON(obj6),
	                doToJSON(obj7),
	                doToJSON(obj8),
	                doToJSON(obj9),
	                doToJSON(obj0)
	            );
	        }
        } catch(exception){
        	alert('[popupWin callback] execute callback function has error : ' + exception);
	    }
    }
    
    function doToJSON(obj){
        if(obj === null || obj === undefined || isBasicType(obj)){
            return obj;
        }
        try{
            var json_str;
            if( window.Prototype && window.Object && isFunction(window.Object.toJSON)){
                json_str = window.Object.toJSON(obj);
                do{
                    json_str = json_str.evalJSON();
                }while(isBasicType(json_str));
                return json_str;
            }else if(window.JSON && isFunction(JSON.stringify)){
                json_str = JSON.stringify(obj);
                do{
                    json_str =  JSON.parse(json_str.evalJSON);
                }while(isBasicType(json_str));
                return json_str;
            }else{
                window.console && console.log('[popupWin][doToJSON] no method to format JSON');
            }
        }catch(e){
            window.console && console.log('[popupWin][doToJSON] object format to JSON Error: '+e.description);
        }
        return obj;
    }
    
    var changeTop, changeLeft, changeRight, changeBottom , isMove;
    var adj_x , adj_y ;
    function doAdjWindow(e){
        var target = getEventTarget(e);
        
        if(target.tagName == 'BUTTON' ){ return; }
        
        var target_id = target.getAttribute('id') || '';
        
        changeTop =  target_id.indexOf('Top') >= 0;
        changeLeft =  target_id.indexOf('Left') >= 0;
        changeRight =  target_id.indexOf('Right') >= 0;
        changeBottom =  target_id.indexOf('Bottom') >= 0;
        
        isMove = !(changeTop || changeLeft || changeRight || changeBottom );
        
        if(isMove && hasClassName( ui_element , 'no-move')){
            return;
        }else if(!isMove && hasClassName( ui_element , 'no-resize')){
            return;
        }
        
        var top_position = ui_block.offsetTop;
        var left_position = ui_block.offsetLeft;
        var adj_org_w = ui_block.offsetWidth;
        var adj_org_h = ui_block.offsetHeight;
        
        adj_window.style.top = top_position + 'px';
        adj_window.style.left = left_position + 'px';
        adj_window.style.width = adj_org_w + 'px';
        adj_window.style.height = adj_org_h + 'px';
        
        ui_block.style.display = 'none';
        adj_cover.style.display = 'block';
        
        var pos = window.event;
        adj_x = pos.clientX - left_position;
        adj_y = pos.clientY - top_position;
        
        var cursor = getStyle(target,'cursor');
        adj_window.style.cursor = cursor;
        adj_cover.style.cursor = cursor;
    }
    
    function finishAjdWindow(e){
        if(getStyle(adj_cover,'display')=='none'){ return; }
        
        var width = adj_window.offsetWidth;
        var height = adj_window.offsetHeight;
        
        ui_block.style.top = Math.max(adj_window.offsetTop,0)+'px';
        ui_block.style.left = Math.max(adj_window.offsetLeft,0)+'px';
        
        
        adj_cover.style.display = 'none';
        ui_block.style.display = '';
        
        var iframe = main.getElementsByTagName('iframe');
        iframe = iframe[iframe.length -1 ];
        
        iframe.style.width = (width - (ui_block.offsetWidth - iframe.offsetWidth))+'px';
        iframe.style.height = (height - (ui_block.offsetHeight - iframe.offsetHeight))+'px';
        
        cancelEventBubble(e);
    }
    
    function moveAjdWindow(e){
        var pos = window.event;
        var pos_x = pos.clientX;
        var pos_y = pos.clientY;
        if(isMove){
            adj_window.style.top = Math.max(pos_y - adj_y , -adj_y ) + 'px';
            adj_window.style.left = Math.max(pos_x- adj_x , -adj_x ) + 'px';
        }
        if(changeBottom){
            adj_window.style.height = Math.max( pos_y - adj_window.offsetTop , configs.minHeight ) +'px';
        }
        if(changeRight){
            adj_window.style.width = Math.max( pos_x - adj_window.offsetLeft , configs.minWidth ) +'px';
        }
        if(changeTop){
            var k = (adj_window.offsetTop + adj_window.offsetHeight - 4 );
            adj_window.style.top = Math.max(pos_y , 0 ) + 'px';
            adj_window.style.height = Math.max( k - pos_y , configs.minHeight ) +'px';
        }
        if(changeLeft){
            var k = (adj_window.offsetLeft + adj_window.offsetWidth - 4 );
            adj_window.style.left = Math.max(pos_x , 0 ) + 'px';
            adj_window.style.width = Math.max( k - pos_x , configs.minWidth ) +'px';
        }
    }
    


	/**
	 *  @public
	 *	@class §Q¥Î DIV ²£¥Íµøµ¡
	 *	@param text (String): ©óµe­±©Ò­nÅã¥Ü¤§¤å¦r¡C
	 *	@param config (Map)
	 *	@param config.src (String) : ©Ò­n³sµ²¤§ url¡C
	 *	@param config.width (Number) : ¶}±Òµøµ¡¼e«×¡C
	 *	@param config.height (Number) : ¶}±Òµøµ¡¼e«×¡C
	 *	@param config.left (Number) : ¶}±Òµøµ¡¶ZÂ÷¥ªÃä¤§¼e«×¡C
	 *	@param config.top (Number) : ¶}±Òµøµ¡¶ZÂ÷¤W¤è¤§°ª«×¡C
	 *	@param config.scrolling (String<yes/no>): ¬O§_²£¥Í scroll bar¡C
	 *	@param config.parameters (Array/Map/String) : ¶Ç»¼°Ñ¼Æ¡C
	 *	@param config.closeConfirm (Map)
	 *	@param config.closeConfirm.assignConfirmType (Boolean) : §PÂ_·í¥X²{ confirm ®É¡AÃö³¬ popupWin ªº®É¾÷¬O¦bÂI¿ï¡u½T»{ (true)¡vÁÙ¬O¡u¨ú®ø (false)¡v¡F­Y¤£³]©w«h¹w³]¬°¡u½T»{¡v¡C 
	 *	@param config.closeConfirm.msg (String) : confirm ®É©ÒÅã¥Ü¤§°T®§¤º®e¡C
     *	@param config.title (String) : µøµ¡¼ÐÃD¡C
     *	@param config.resizable (boolean) : ¨Ï¥ÎŽÍ¥i§_½Õ¾ã¤j¤p¡C
     *	@param config.movable (boolean) : ¨Ï¥ÎŽÍ¥i§_²¾°Êµøµ¡¡C
     *	@param config.closeBtn (boolean) : µøµ¡¤W¤è¬O§_Åã¥ÜÃö³¬«ö¶s¡C
     *	@param config.isFullPopup (boolean) : ¬O§_¥þµøµ¡Åã¥Ü¡C
	 **/
    entity.createPopupLink = function( text, inConfig, inWidth, inHeight, inLeft, inTop, inScrolling, inParameters )
    {

		var config;
    	if( isString(inConfig) ) { /* ¦¹¬q³B²z¬O´£¨Ñµ¹°ê»Ú§ë¸ê³¡¥÷¤l¨t²Î¨Ï¥ÎÂÂª©ªº popupWin ¨Ï¥Î */
			config = {
				src: inConfig,
				width: inWidth,
				height: inHeight,
				left: inLeft,
				top: inTop,
				scrolling: inScrolling,
				parameters: inParameters
            };
	    } else if (isObject(inConfig )){
			config = inConfig;
	    } else {
            window.console && window.console.log('[popupWin][createPopupLink] inConfig Error; ');
            return;
        }

        var link = document.createElement( 'a' );
	        link.setAttribute('href','#');
	        link.innerHTML = text;
	        link.onclick = function( event ) { entity.popup( config ); };
        return link;
    }
    
    /**
	 *  @public
	 *	@class §Q¥Î DIV ²£¥Íµøµ¡
	 *	@param text (String): ©óµe­±©Ò­nÅã¥Ü¤§¤å¦r¡C
	 *	@param config (Map)
	 *	@param config.src (String) : ©Ò­n³sµ²¤§ url¡C
	 *	@param config.width (Number) : ¶}±Òµøµ¡¼e«×¡C
	 *	@param config.height (Number) : ¶}±Òµøµ¡¼e«×¡C
	 *	@param config.left (Number) : ¶}±Òµøµ¡¶ZÂ÷¥ªÃä¤§¼e«×¡C
	 *	@param config.top (Number) : ¶}±Òµøµ¡¶ZÂ÷¤W¤è¤§°ª«×¡C
	 *	@param config.scrolling (String<yes/no>): ¬O§_²£¥Í scroll bar¡C
	 *	@param config.parameters (Array/Map/String) : ¶Ç»¼°Ñ¼Æ¡C
	 *	@param config.closeConfirm (Map)
	 *	@param config.closeConfirm.assignConfirmType (Boolean) : §PÂ_·í¥X²{ confirm ®É¡AÃö³¬ popupWin ªº®É¾÷¬O¦bÂI¿ï¡u½T»{ (true)¡vÁÙ¬O¡u¨ú®ø (false)¡v¡F­Y¤£³]©w«h¹w³]¬°¡u½T»{¡v¡C 
	 *	@param config.closeConfirm.msg (String) : confirm ®É©ÒÅã¥Ü¤§°T®§¤º®e¡C
     *	@param config.title (String) : µøµ¡¼ÐÃD¡C
     *	@param config.resizable (boolean) : ¨Ï¥ÎŽÍ¥i§_½Õ¾ã¤j¤p¡C
     *	@param config.movable (boolean) : ¨Ï¥ÎŽÍ¥i§_²¾°Êµøµ¡¡C
     *	@param config.closeBtn (boolean) : µøµ¡¤W¤è¬O§_Åã¥ÜÃö³¬«ö¶s¡C
     *	@param config.isFullPopup (boolean) : ¬O§_¥þµøµ¡Åã¥Ü¡C
	 **/
    entity.createPopupButton = function( text, inConfig, inWidth, inHeight, inLeft, inTop, inScrolling, inParameters )
    {

		var config;
    	if( isString(inConfig) ) { /* ¦¹¬q³B²z¬O´£¨Ñµ¹°ê»Ú§ë¸ê³¡¥÷¤l¨t²Î¨Ï¥ÎÂÂª©ªº popupWin ¨Ï¥Î */
			config = {
				src: inConfig,
				width: inWidth,
				height: inHeight,
				left: inLeft,
				top: inTop,
				scrolling: inScrolling,
				parameters: inParameters
            };
	    } else if (isObject(inConfig )){
			config = inConfig;
	    } else {
            window.console && window.console.log('[popupWin][createPopupLink] inConfig Error; ');
            return;
        }

        var button = document.createElement( 'button' );
	        button.setAttribute('type' , 'button');
            button.setAttribute('class' , 'button');
            addClassName(button,'button');
            button.innerHTML = text;
	        button.onclick = function( event ) { entity.popup( config ); };
        return button;
    }

	/**
	 *  @public
	 *	@class §Q¥Î DIV ²£¥Íµøµ¡¡]»P¥Ø«e©ÒÂsÄý¤§­¶­±¤j¤p¬Û¦P¡^
	 *	@param text (String): ©óµe­±©Ò­nÅã¥Ü¤§¤å¦r¡C
	 *	@param config (Map)
	 *	@param config.src (String) : ©Ò­n³sµ²¤§ url¡C
	 *	@param config.parameters (Array/Map/String) : ¶Ç»¼°Ñ¼Æ¡C
	 **/
	entity.createFullPopupLink = function( text, inConfig, inScrolling, inParameters )
    {

		var config;
    	if( isString(inConfig) ) { /* ¦¹¬q³B²z¬O´£¨Ñµ¹°ê»Ú§ë¸ê³¡¥÷¤l¨t²Î¨Ï¥ÎÂÂª©ªº popupWin ¨Ï¥Î */
			config = {
				src: inConfig,
				scrolling: inScrolling,
				parameters: inParameters
            };
	    } else if (isObject(inConfig )){
			config = inConfig;
	    } else {
            window.console && window.console.log('[popupWin][createFullPopupLink] inConfig Error; ');
            return;
        }
        
        var link = document.createElement( "a" );
	        link.href = "#";
	        link.innerHTML = text;
	        link.onclick = function( event ) { entity.fullPopup( inConfig );};
        return link;
    }
    
    /**
	 *  @public
	 *	@class §Q¥Î DIV ²£¥Íµøµ¡¡]»P¥Ø«e©ÒÂsÄý¤§­¶­±¤j¤p¬Û¦P¡^
	 *	@param text (String): ©óµe­±©Ò­nÅã¥Ü¤§¤å¦r¡C
	 *	@param config (Map)
	 *	@param config.src (String) : ©Ò­n³sµ²¤§ url¡C
	 *	@param config.parameters (Array/Map/String) : ¶Ç»¼°Ñ¼Æ¡C
	 **/
    entity.createFullPopupButton = function( text, inConfig, inScrolling, inParameters )
    {

		var config;
    	if( isString(inConfig) ) { /* ¦¹¬q³B²z¬O´£¨Ñµ¹°ê»Ú§ë¸ê³¡¥÷¤l¨t²Î¨Ï¥ÎÂÂª©ªº popupWin ¨Ï¥Î */
			config = {
				src: inConfig,
				scrolling: inScrolling,
				parameters: inParameters
            };
	    } else if (isObject(inConfig )){
			config = inConfig;
	    } else {
            window.console && window.console.log('[popupWin][createFullPopupLink] inConfig Error; ');
            return;
        }
        
        var button = document.createElement( 'button' );
	        button.setAttribute('type' , 'button');
            button.setAttribute('class' , 'button');
            addClassName(button , 'button');
            button.innerHTML = text;
	        button.onclick = function( event ) { entity.fullPopup( inConfig );};
            
        return button;
    }

	/**
	 * ¼W¥[ Function¡uchangeTopScrollHeight¡v¡A´£¨Ñ¦Û°Ê½Õ¾ã©Î«ü©w³Ì¤W¼h¤§­¶­±°ª«×
	 * @param changeHeight «ü©w°ª«×
     * 2016- ¥\¯à¼o°£
	 */
	entity.changeTopScrollHeight = function(changeHeight){}

	/**
	 * ¼W¥[ Function¡urollBackTopScrollHeight¡v¡A´£¨Ñ¦^´_³sµ²¶i¤J­¶­±®É¡A³Ì¤W¼h¤§ªì©l­¶­±°ª«×
     * 2016- ¥\¯à¼o°£
	 */
	entity.rollBackTopScrollHeight = function(){}
    
    
    /**
	 *	@public
	 *	@class §Q¥Î window open ¶}±Òµøµ¡ <br>
	 *         ¹w³]µLª¬ºA¦C¡Btoolbar¡Bmenubar
	 *	       Ex : 
	 *              // ³sµ²¦Ü«ü©w¥æ©ö¡A¨Ã¶Ç»¼°Ñ¼Æ
	 *	            popupWin.windowOpen('<%=dispatcher%>/XXA0_0100/query',{
	 *	                'parameters': {'QUERY_KEY': 'TEST'}
	 *	            });
	 *
	 *              // «ü©w opts¡uattributes¡v¤º¤§ÄÝ©Ê®É¡A«h©Ò¶}±Ò¤§µøµ¡·|¨Ì¨äÅÜ¤Æ
	 *	            popupWin.windowOpen('<%=dispatcher%>/XXA0_0100/query',{
	 *	                'parameters': {'QUERY_KEY': 'TEST'},
	 *	                'attributes': {'toolbar': 'yes', 'width': '800px', 'height': '600px'}
	 *	            });
	 *
	 *              // «ü©w opts¡uwindowName¡v®É¡A¶}±Ò¤§µøµ¡¦b¤£¦P¦Wªº±¡ªp¤U¡A«h¤£·|¦@¥Î
	 *	            popupWin.windowOpen('<%=dispatcher%>/XXA0_0100/query',{
	 *	                'parameters': {'QUERY_KEY': 'TEST'},
	 *	                'attributes': {'width': '800px', 'height': '600px'},
	 *	                'windowName': 'newWindow'
	 *	            });
	 *
	 *              // «ü©w opts¡ufullScreen¡v®É¡A«h¶}±Ò¥þ¿Ã¹õµøµ¡¡]¦¹®É¹w³]¤£¥i½Õ¾ãµøµ¡¤j¤p¡A¥i³]©w attributes resizable ±N¨ä³]©w¬°¥i½Õ¾ã¡^
	 *	            popupWin.windowOpen('<%=dispatcher%>/XXA0_0100/query',{
	 *	                'parameters': {'QUERY_KEY': 'TEST'},
	 *	                'fullScreen': true
	 *	            });
	 *
	 *	@param url (String) : ¤é´Á1
	 *	@param opts(Map) :
	 *	       opts.parameters (Map) : ¶Ç»¼°Ñ¼Æ
	 *	       opts.attributes (Map) : window open ÄÝ©Ê¡]¦p¡Gstatus, toolbar, menubar, resizable, width, height, top, left ...µ¥¡^
	 *	       opts.windowName (String) : ¶}±Òµøµ¡ªº¦WºÙ
	 *	       opts.fullScreen (Boolean) : ¬O§_¥þ¿Ã¹õ¡A¹w³]¬° false
	 **/
	entity.windowOpen = function(url, opts){

		opts = opts || {};

		var params = opts.parameters || {};
		var fullScreen = opts.fullScreen === true;
		var attrs = {'status':'no','toolbar':'no','menubar':'no','resizable':fullScreen?'no':'yes', 'scrollbars': 'yes'};

		var inputAttrs = opts.attributes;
		if(inputAttrs){
			for(var k in inputAttrs){
				attrs[k]=inputAttrs[k];
			}
		}

		if(fullScreen){
			attrs['width']=screen.availWidth;
			attrs['height']=screen.availHeight;
			attrs['top']=0;
			attrs['left']=0;
		}

		var windowName = opts.windowName || 'popupWinWindowOpen';

		var windowOpenAttrs = '';
		for(var attrKey in attrs){
			if(windowOpenAttrs.length>0){ windowOpenAttrs +=','; }
			windowOpenAttrs += attrKey+'='+attrs[attrKey];
		}

		var formId = '_popupWin_windowOpen_form';
		var theForm = document.getElementById(formId);

		if (!theForm) {
            theForm = document.createElement('form');
            theForm.id = formId;
            theForm.method = 'post';
            theForm.style.display = 'none';
            document.body.appendChild(theForm);
        }

        theForm.action = url;
        theForm.innerHTML = '';

        for ( var k in params) {
            var oInput = document.createElement('input');
                oInput.type = 'hidden';
                oInput.name = k;
                oInput.value = params[k];
            theForm.appendChild(oInput);
        }

		try{
			var eBAF_loginSystemInfo = top['eBAF_loginSystemInfo'];
			var eBAF_UserObject_Flag = top['eBAF_UserObject_Flag'];
			if(eBAF_loginSystemInfo){
	            var oInput = document.createElement('input');
                    oInput.type = 'hidden';
                    oInput.name = 'eBAF_loginSystemInfo';
                    oInput.value = eBAF_loginSystemInfo;
                theForm.appendChild(oInput);
			}
			if(eBAF_UserObject_Flag){
                var oInput = document.createElement('input');
                    oInput.type = 'hidden';
                    oInput.name = 'eBAF_UserObject_Flag';
                    oInput.value = eBAF_UserObject_Flag;
                theForm.appendChild(oInput);
            }
        } catch (e) {
        }

        theForm.setAttribute('target', windowName);
        window.open('', windowName, windowOpenAttrs, true);
        theForm.submit();

	};
	
	
    //utilitys
    function getPX(value, default_value , base_value , max_ratio){
        var ratio = null , counten = null;
        if((/^[0|1]\.\d*/).test(value)){
            ratio = Math.min(value,max_ratio);
            counten = base_value*ratio;
        }else if((/^\d\d*?\.?\d*?%/).test(value)){
            ratio = Math.min(parseInt(value)/100,max_ratio);
            counten = base_value*ratio;
        }else if((/^\d\d*?\.?\d*?[px]?/).test(value)){
            counten = value; 
        }else if(default_value){
            return getPX(default_value,null,base_value,max_ratio);
        }else{
            return [(base_value-100),null]
        }
        return [parseInt(counten),ratio];
    }
	

    function getBoolean(value, default_boolean, params) {
        if (isFunction(value)) {
            if (params && isArray(params)) {
                value = value.apply(this, params);
            } else {
                value = value();
            }
        }
        if (value !== (!default_boolean)) {
            return default_boolean;
        }
        return !default_boolean;
    }

    function getElement(testElement, whenNULL, function_params, function_input_params_part) {

        if (isElement(testElement, true)) {
            return testElement;
        }

        if (isFunction(testElement)) {
            if (function_params) {
                if (function_input_params_part && isArray(function_params)) {
                    testElement = testElement.apply(this, function_params);
                } else {
                    testElement = testElement.call(this, function_params);
                }
            } else {
                testElement = testElement();
            }
        }

        if (!isElement(testElement, true)) {
            if (!isBasicType(testElement)) {
                testElement = isBasicType(whenNULL) ? whenNULL : "¡@";
            }

            /* Âà¦¨html¿é¥X¡A¦]¬°fragment¤£¯à¨Ï¥ÎinnerHTML¡A©Ò¥H¥ý«Ø¥ß¤@­Óspan¡A¦A±Nspan¤UªºªF¦è·h²¾fragment */
            var temp_span = createElement("span");
            temp_span.innerHTML = testElement;

            testElement = document.createDocumentFragment();
            while (temp_span.hasChildNodes()) {
                testElement.appendChild(temp_span.firstChild);
            }
        }

        return testElement;
    }
	
	function getElementsBySelectorAt(cssSelector , parent){
		if(!window.Prototype || !Element || !Element.getElementsBySelector){ 
			window.console && window.console.log('[popupWin][getElementsBySelector] prototype.js did not import!');
			return []; 
		}
		return Element.getElementsBySelector( isElement(parent) ? parent : document.body ,  cssSelector );
	}
	
	 function getParentByTagName(node, tagName) {
        if (!isElement(node) || !isBasicType(tagName)) { return null; }
        tagName = tagName.toUpperCase();
        do {
            node = node.parentNode;
            if (!node || node.tagName == "BODY") { return null; }
        } while (node.tagName != tagName);
        return node;
    }

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
    }

   

    function setEventObserve(elem, eventName, func) {
        if (!(isElement(elem) || elem == window || elem == document) || !isString(eventName) || !isFunction(func)) { return; }
        if (window.Prototype) {
            Event.observe(elem, eventName, func);
        } else if (elem.removeEventListener) {						//DOM2±µ¤f with bubble
            elem.addEventListener(eventName, func, false);
        } else if (elem.detachEvent) {					//IE DOM2±µ¤f  with bubble
            elem.attachEvent('on' + eventName, func);
        } else {											//DOM0±µ¤f without bubble
            if (window.Prototype) {
                Event.observe(elem, eventName, func);
            } else {
                elem['on' + eventName] = func;
            }
        }
    }

    function stopEventObserving(elem, eventName, func) {
        if (!(isElement(elem) || elem == window || elem == document) || !isString(eventName)) { return; }
        if (window.Prototype) {
            Event.stopObserving(elem, eventName, func);
        } else if (elem.removeEventListener) {						//DOM2±µ¤f with bubble
            elem.removeEventListener(eventName, func);
        } else if (elem.detachEvent) {						//IE DOM2±µ¤f  with bubble
            elem.detachEvent('on' + eventName, func);
        } else {											//DOM0±µ¤f without bubble
            if (window.Prototype) {
                Event.stopObserving(elem, eventName);
            } else {
                elem['on' + eventName] = null;
            }
        }
    }

    function cancelEventBubble(e) {
        if (!e){
            e = window.event;
        }
        if (!e){
            return;
        }
        //IE9 & Other Browsers 
        if (e.stopPropagation) {
            e.stopPropagation();
        }
            //IE8 and Lower 
        else {
            e.cancelBubble = true;
        }
    }

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
                        if(window.Prototype){
                            Event.fire(elem, eventName);
                        }else{
                            throw 'level 1';
                        }
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

    function isElement(node, allowFragment) {
        if(!node){ 
			return false;
		}else if( getType(node).match(/\[object HTML.*?Element\]/)){ // DOM
			return true;
		}else if ( getType(node) === "[object Text]" ){ // TEXT NODE 
			return true;
		}else if ( allowFragment && getType(node) === "[object DocumentFragment]" ){ //Fragment
			return true;
		}
		//FOR OLD BROWSER...
		//nodeType == 1 --> DOM
        //nodeType == 3 --> TextNode
        //nodeType == 11 --> Fragment
        return !!(node.tagName && (node.nodeType === 1 || node.nodeType === 3 || allowFragment && node.nodeType === 11));
    }

    function isString(node) {
        return !!(node && getType(node) === '[object String]');
    }

    function isNumeric(node) { return isBasicType(node) && (/^-?(0|[1-9]\d*|(?=\.))(\.\d+)?$/.test(node)); }

    function isBasicType(node) {
        var type = typeof (node);
        return !!(type === 'string' || type === 'number' || type === 'boolean');
    }

    function isArray(node) {
        return !!(node && getType(node) === '[object Array]' && isNumeric(node.length));
    }

    function isObject(node) {
        return !!(node && getType(node) == '[object Object]' && !isArray(node) && !isFunction(node));
    }

    function isFunction(node) {
        return !!(node && getType(node) === '[object Function]');
    }

    function isInArray(array, obj) {
        if (!isArray(array)) { return -1; }
        for (var i = 0 ; i < array.length ; i++) {
            if (("" + array[i]) === ("" + obj)) { return i; }
        }
        return -1;
    }

    function getType(node) {
		if ( node === null ) { return "null"; }
        if ( node === undefined) { return "undefined"; }
        return Object ? Object.prototype.toString.apply(node) : "";
    }

    function getInteger(val, default_int) {
        default_int = default_int || 0;
        if (!isNumeric(val)) { return default_int; }
        return parseInt(val, 10);
    }

    function getCellAttributeToInteger(cell, attr, default_int) {
        default_int = default_int || 0;
        if (!isElement(cell)) { return default_int; }
        var val = cell.getAttribute(attr);
        if (!isNumeric(val)) { return default_int; }
        return parseInt(val, 10);
    }

    function addClassName(node, className) {
        if (isBasicType(className) && !hasClassName(node, className)) {
            node.className = trim((node.className || "") + " " + className);
        }
    }

    function getClassName(node) {
        var classes = node.className;
        if (!isBasicType(classes)) { return []; }
        classes = trim(classes.replace(/\s\s/g, " "));
        return stringToArray(classes, " ");
    }

    function removeClassName(node, compare) {
        if (!isBasicType(compare)) { return false; }
        var classes = node.className;
        if (!isBasicType(classes)) { return false; }
        compare = compare.replace(/\\/gi, "\\\\");

        var reg = new RegExp("(^" + compare + "\\s)|(\\s" + compare + "\\s)|(\\s" + compare + "$)|(^" + compare + "$)", "gi");
        node.className = classes.replace(reg, " ").replace(/\s\s/gi, " ");
    }

    function hasClassName(node, compare) {
        if (!isElement(node) || !isBasicType(compare)) { return false; }
        var classes = node.className;
        if (!isBasicType(classes)) { return false; }
        compare = compare.replace(/\\/gi, "\\");

        var reg = new RegExp("(^" + compare + "\\s)|(\\s" + compare + "\\s)|(\\s" + compare + "$)|(^" + compare + "$)", "gi");

        return classes.match(reg) != null;
    }

    function stringToArray(stringValue, splitChar) {
        if (isArray(stringValue)) { return stringValue; }
        if (!isBasicType(stringValue)) { return [stringValue]; }
        var strings = (stringValue+'').split(splitChar);
        var rtnArray = [];
        for (var i = 0 ; i < strings.length ; i = i + 1) {
            var str = trim(strings[i]);
            if (isBasicType(str) && str!=='' && str != "undefined" && str != "null") {
                rtnArray.push(str);
            }else{
				rtnArray.push(null);
			}
        }
        return rtnArray;
    }

    function trim(value) {
        return isString(value) ? value.replace(/(^\s*)|(\s*$)/g, "").replace(/^&nbsp;|&nbsp;$/g, "") : value;
    }

    function createElement(node, attrs, styles, classes, events, appendAt, content, createDocument) {

        if (!isElement(node)) {
            if (isString(node)) {
                node = (createDocument || document).createElement(node);
            } else {
                return;
            }
        }

        if (isObject(attrs)) {
            for (var key in attrs) {
                if (isBasicType(attrs[key])) {
                    var lowerCaseKey = (key + "").toLowerCase();
                    if(lowerCaseKey == 'class' || lowerCaseKey == 'className'){
                        node.className = attrs[key];
                    }else if(lowerCaseKey == 'innerhtml' || lowerCaseKey == 'innertext' || lowerCaseKey == 'contenttext'){
                        if(content === null){
                            content = attrs[key];
                        }
                    }else if ( lowerCaseKey != 'value') {
                        node.setAttribute(key, attrs[key]);
                    } else {
                        if (isFunction(node.setValue)) {
                            node.setValue(attrs[key]);
                        } else {
                            node.value = attrs[key];
                        }
                    }
                }
            }
        }

        if (isObject(styles)) {
            for (var key in styles) {
                if (isBasicType(styles[key])) {
                    node.style[key] = styles[key];
                }
            }
        }

        if (isArray(classes)) {
            for (var i = 0  ; i < classes.length ; i = i + 1) {
                addClassName(node, classes[i]);
            }
        }

        if (isObject(events)) {
            for (var key in events) {
                setEventObserve(node, key, events[key]);
            }
        }

        if (content !== null && content !== undefined && content !== "null" && content !== "undefined") {
            node.appendChild(getElement(content));
        }

        if (isElement(appendAt, true)) {
            appendAt.appendChild(node);
        }

        return node;
    }

    function createFragment(elems) {
        var fragment = document.createDocumentFragment();
        if (isArray(elems)) {
            for (var i = 0 ; i < elems.length ; i = i + 1) {
                var elem = getElement(elems[i]);
                if (elem) {
                    fragment.appendChild(elem);
                }
            }
        }
        return fragment;
    }

    function cloneObject(orgObject) {
        var returnObject;
        var hasValue = false;
        if (isElement(orgObject)) {
            hasValue = false;
        }else if (isArray(orgObject)) {
            returnObject = [];
            for (var i = 0 ; i < orgObject.length ; i = i + 1) {
                var cloneData = cloneObject(orgObject[i]);
                //if(cloneData === null){ continue; }
                returnObject[i] = cloneData;
            }
            hasValue = true;
        } else if (isObject(orgObject)) {
            returnObject = {};
            for (var key in orgObject) {
                var cloneData = cloneObject(orgObject[key]);
                //if (cloneData === null) { continue; }
                returnObject[key] = cloneData;
            }
            hasValue = true;
        } else if (isBasicType(orgObject)) {
            returnObject = orgObject;
            hasValue = true;
        }

        if (!hasValue) { return null; }

        return returnObject;
    }

    function getStyle(elem, name) {
        if (elem.style[name])
            return elem.style[name];
        else if (elem.currentStyle)
            return elem.currentStyle[name];
        else if (document.defaultView && document.defaultView.getComputedStyle) {
            name = name.replace(/([A-Z])/g, "-$1");
            name = name.toLowerCase();
            var s = document.defaultView.getComputedStyle(elem, "");
            return s && s.getPropertyValue(name);
        }
        else
            return null;
    }

};


var popupWin = new PopupWinUI();