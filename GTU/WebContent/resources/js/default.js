var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;
	
//彈跳視窗關閉時要回到index.xhtml
function goBackIndex() {
	window.close();
	if (opener) {
		opener.focus();
		opener.location = basePath + "/faces/pages/common/index.xhtml";
	}
}

//彈跳視窗關閉時要更新原畫面
function goBack() {
	window.close();
	if (opener) {
		opener.focus();
		opener.location.reload();
	}
}

// 設定ReplaceAll方法
String.prototype.ReplaceAll = function(AFindText, ARepText) {
	raRegExp = new RegExp(AFindText, "g");
	return this.replace(raRegExp, ARepText);
}
	
var getElementsByClassName = function (searchClass, node,tag) { 
	if(document.getElementsByClassName){ 
		return document.getElementsByClassName(searchClass);
	}else{ 
		node = node || document; 
		tag = tag || "*"; 
		var classes = searchClass.split(" "), 
		elements = (tag === "*" && node.all)? node.all : node.getElementsByTagName(tag), 
		patterns = [], 
		returnElements = [], 
		current, 
		match; 
		var i = classes.length; 
		while(--i >= 0){ 
			patterns.push(new RegExp("(^|\\s)" + classes[i] + "(\\s|$)")); 
		} 
		var j = elements.length; 
		while(--j >= 0){ 
			current = elements[j]; 
			match = false; 
			for(var k=0, kl=patterns.length; k<kl; k++){ 
				match = patterns[k].test(current.className); 
				if (!match) break; 
			} 
			if (match) returnElements.push(current); 
		} 
		return returnElements; 
	} 
}

//清除className 的欄位值
function clearClassNameValue(className){
	var collections = getElementsByClassName(className);
	var elsLen = collections.length; 
	for (i = 0; i < elsLen; i++) {
		document.getElementById(collections[i].id).value = "";
	}
}

var isFireFox = window.location.protocol.toString() + "//" + window.location.host.toString() + "#{request.contextPath}" + "/faces/";

if (window.XMLHttpRequest) { // Mozilla, Safari,...
      http_request = new XMLHttpRequest();
    } else if (window.ActiveXObject) { // IE
      isFireFox = "./";
      try {
        // 新版的 IE
        http_request = new ActiveXObject("Msxml2.XMLHTTP");
      } catch (e) {
        try {
          // 舊版的 IE
          http_request = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (e) {}
      }
}

/*欄位對齊一條線*/
jQuery(document).ready(function() {
    var max = 0;
    jQuery(".text").each(function(){
        if (jQuery(this).width() > max)
            max = jQuery(this).width();   
    });
    jQuery(".text").width(max);
});


//清空記事例欄位
function noteClearJS(obj){
	var movehouseholdmark = document.getElementById(obj.id);
	if(movehouseholdmark.value != null && movehouseholdmark.value.length > 8){
		movehouseholdmark.value = "";
	}
}

//放大鏡
jQuery(function() {
	resetImg();
});

//jsf若使用f:ajax render則被更新的對象，連原本綁定的jqueryzoom Function都會被刷掉，這裡重新綁定。
function resetImg(){
	 jQuery(".jqzoom").jqueryzoom({
	        xzoom:280,  // 放大图的宽
	        yzoom:350,  // 放大图的高
	        offset:10,   // 放大图距离原图的位置
	        position:'left'  // 放大图在原图的右边(默认为right)
	 });
}

//lightbox onLoad 不需要，因為畫面上可能無此元素，onLoad讓primeafces自已綁定，render 後才自已做 resetLightBox
//jQuery(function() {
//	jQuery(".lightboxStyle a").colorbox({transition:'elastic',width:'65%',height:'80%',iframe:true});
//	resetImg();
//});

//jsf若使用f:ajax render則被更新的對象，連原本綁定的jquery colorbox都會被刷掉，這裡重新綁定。
//rel:'', 還是要設群組，只是原本就有綁，所以名稱為空''
function resetLightBox(){
	jQuery(".lightboxStyle a").colorbox({transition:'elastic',width:'65%',height:'80%',iframe:true,rel:''});
	resetImg();
}