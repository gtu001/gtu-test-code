
document.addEventListener("DOMContentLoaded", function(){
	var mousePositionHandler = new function(){
		document.captureEvents(Event.MOUSEMOVE);
		var rtnJson = {};
		document.onmousemove = function(e){
			var x = (window.Event) ? e.pageX : event.clientX + 
					(document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
			var y = (window.Event) ? e.pageY : event.clientY + 
					(document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
			
			if((window.innerWidth - x) < 100) {
				x = x - 120;
			}
			if((window.innerHeight - y) < 40) {
				y = y - 45;
			}
			
			rtnJson['x'] = x;
			rtnJson['y'] = y;
		};
		return rtnJson;
	};
	
	function getType(node){
		return node ? {}.toString.call(node) : "undefined";
	}
	
	Element.prototype.removeChild1 = function() {
	    this.parentElement && this.parentElement.removeChild(this);
	};

	HTMLElement.prototype.after = function() {
		var argArr = Array.prototype.slice.call(arguments),
		docFrag = document.createDocumentFragment();
		argArr.forEach(function (argItem) {
			var isNode = argItem instanceof Node;
			docFrag.appendChild(isNode ? argItem : document.createTextNode(String(argItem)));
		});
		this.parentNode.insertBefore(docFrag, this.nextSibling);
	};
	
	var getMsgForSelect = function(ele){
		try{
			if(ele.tagName == "SELECT"){
				return ele.options[ele.selectedIndex].value;
			}	zz
		}catch(e){
		}
		return null;
	};
	
	var getMsg = function(ele){
		var msgAry = new Array();
		if(ele.getAttribute("id")){
        	msgAry.push("id = " + ele.getAttribute("id"));
        }
        if(ele.getAttribute("name")){
        	msgAry.push("name = " + ele.getAttribute("name"));
        }
        if(getMsgForSelect(ele)){
        	msgAry.push("selectValue = " + getMsgForSelect(ele));
        }else if(ele.value){
        	msgAry.push("value = " + ele.value);
        }else if(ele.getAttribute("value")){
        	msgAry.push("value = " + ele.getAttribute("value"));
        }else {
        	msgAry.push("text = " + ele.innerText);
        }
        return msgAry.join("<br/>");
	};

	var hoverShow = function(ele, color, func){
		var div1 = document.createElement("span");
		ele.addEventListener("mousemove", function(e){
			div1.style.left = mousePositionHandler.x + "px";
            div1.style.top = mousePositionHandler.y + "px";
		});
		ele.addEventListener("mouseover", function(e){
			//ele.after(div1);
			document.querySelector("body").appendChild(div1);
		
            var rect = ele.getBoundingClientRect();
            
            var pRect = ele.parentNode.getBoundingClientRect();
            var pLeft = 0;//pRect.left;
            var pTop = 0;//pRect.top;
            
            div1.setAttribute("style", "border-width:3px; border-style:dashed; border-color:" + color + "; padding:5px; font-size:12px; opacity:1.0; filter:alpha(opacity=100); z-index: 10000; float:right; ");
            div1.style.position = "absolute";//absolute
            div1.style.backgroundColor = "WHITE"; 
            
            /*
            div1.style.left = (rect.left + rect.width - pLeft) + "px";
   	        div1.style.top = (rect.top + rect.height - pTop) + "px";
   	        
   	        if((rect.left + rect.width - pLeft) > window.innerWidth - 50){
   	        	//div1.style.left = ((rect.left + rect.width - pLeft) - div1.offsetWidth) + "px";
   	        	div1.style.left = ((rect.left + rect.width - pLeft) - rect.width - 50) + "px";
	        }
   	        */
            
            div1.style.left = mousePositionHandler.x + "px";
            div1.style.top = mousePositionHandler.y + "px";
   	        
            div1.style.visibility = "visible";
            div1.style.display = "block";
            
            div1.innerHTML = func(ele);
            
            var isDiv1MouseIn = false;
            
            div1.addEventListener("mouseover", function(e){
            	isDiv1MouseIn = true;
            });
            div1.addEventListener("mouseout", function(e){
            	isDiv1MouseIn = false;
            });
            
            ele.addEventListener("mouseout", function(e){
//	       		div1.style.visibility = "hidden";
//	       		div1.style.display = "none";
				
				var setOut = function(){
					setTimeout(function(){
						if(!isDiv1MouseIn){
							div1.removeChild1();
						}else{
							setOut();
						}
					}, 200);
				};
				setOut();
	        });
        });
	};

	var singleChkArry1 = ["input", "select", "textarea", "button", "span", "div", "label", "th", "td"];
	for(var i in singleChkArry1){
		var arry = document.getElementsByTagName(singleChkArry1[i]);
		for(var ii = 0 ; ii < arry.length; ii ++){
			var obj = arry[ii];
			
			//Ignore Bootstrap !!
			if(obj.tagName == "DIV"){
				var ignore = false;
				if(obj.getAttribute("id") == "container"){
					ignore = true;
				}else if(obj.classList.contains("content")){
					ignore = true;
				}
				continue;
			}
			
			if(obj.getAttribute("id") || obj.getAttribute("name")){
				hoverShow(obj, "#FFAC55", getMsg);
			}
		}
	}
	
	//特別處理 Message ----------------------------
	var getMsgFromMap = function(title, rtnMap){
		var msgArry = new Array();
		if(title){
			msgArry.push("<font color='red'>"+title+"</font>");
		}
		var keys = Object.keys(rtnMap).sort();
		var html = "<table style='font-size: 10px;'>"
		var tmpArry = [];
		var tdCount = Math.max(Math.floor(keys.length / 10), 1);
		for(var ii = 0 ; ii < keys.length; ii ++){
			if((ii + 1) % tdCount == 0){
				html += "<tr><td>" + tmpArry.join("</td><td>") + "</td></tr>";
				tmpArry = [];
			}
			tmpArry.push(keys[ii] + " = " + rtnMap[keys[ii]]);
		}
		if(tmpArry.length != 0){
			html += "<tr><td>" + tmpArry.join("</td><td>") + "</td></tr>";
		}
		html += "</table>"
		msgArry.push(html);
		return msgArry.join("<br/>");
	};
	
	var showMapConfig = {
							".showAttr" : { title : "顯示Init資料", 
											dataMap : {} // <c:out value='${mapX120JSON}' default='{}' escapeXml='false'/> 
											},
							".showHidden" : { title : "顯示Hidden欄位", 
											dataMap : function(){
												var formMap = {};
												var arry = document.querySelectorAll("[type=hidden]");
												for(var ii = 0 ; ii < arry.length; ii ++){
													var name = arry[ii].getAttribute("name") ? arry[ii].getAttribute("name") : arry[ii].getAttribute("id");
													if(name){
														formMap[name] = arry[ii].value;
													}
												}
												return formMap;
											 }
											}
						 };
						
	for(var queryKey in showMapConfig){
		if(document.querySelector(queryKey)){
			var tag = document.querySelector(queryKey);
			tag.setAttribute("queryKey", queryKey);
			hoverShow(tag, "#d0dfef", function(ele){
				var config = showMapConfig[ele.getAttribute("queryKey")];
				var obj = config.dataMap;
				if(getType(obj) == '[object Function]'){
					obj = obj();
				}
				return getMsgFromMap(config.title, obj);
			});
		} 
	}
});