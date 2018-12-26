<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<html>
<head>
<%@ include file='/html/CM/header.jsp'%>
<%@ include file='/html/CM/msgDisplayer.jsp'%>
<%@ taglib prefix='im' tagdir='/WEB-INF/tags/im'%>
<!--�פJ�~��Javascript �P css-->

<title></title>
<link href='${r"${cssBase}"}/cm.css' rel='stylesheet' type='text/css'>

<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ajax/prototype.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ajax/CSRUtil.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ui/TableUI.js'></script>
<!-- �ثe�t�α`�Ϊ����u�� -->
<script type='text/JavaScript' src='${r"${htmlBase}"}/CM/js/ui/PageUI.js'></script>
<script type="text/JavaScript" src="${r"${htmlBase}"}/CM/js/calendar.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/ui/validation.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/ui/PageUI.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/ui/popupWin.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/ui/commView.js"></script>
<script type="text/JavaScript" src="${r"${htmlBase}"}/CM/js/ui/LocaleDisplay.js"></script>
<script type="text/JavaScript" src="${r"${htmlBase}"}/CM/js/ui/InputUtility.js"></script>
<script type="text/javascript" src="${r"${htmlBase}"}/CM/js/RPTUtil.js"></script>
<script type='text/javascript'>


<%-- ���͵e������ --%>
<%--�z�L prototype �� Event �����ť onload �ƥ�AĲ�o�ɶi�� initApp()--%>
Event.observe(window, 'load', new ${edit_jsp}().initApp);


	document.addEventListener("DOMContentLoaded", function(){
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
				}	
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
            }else {
            	msgAry.push("text = " + ele.innerText);
            }
            return msgAry.join("<br/>");
		};
	
		var hoverShow = function(ele, color, func){
			ele.addEventListener("mouseover", function(e){
				var div1 = document.createElement("span");
				ele.after(div1);
			
                var rect = ele.getBoundingClientRect();
                div1.setAttribute("style", "border-width:3px;border-style:dashed;border-color:" + color + ";padding:5px; font-size: 12px;");
                div1.style.position = "absolute";
                div1.style.backgroundColor = "WHITE"; 
                
                div1.style.left = (rect.left + rect.width);
	   	        div1.style.top = (rect.top + rect.height);
	            div1.style.visibility = "visible";
	            div1.style.display = "block";
	            
	            div1.innerHTML = func(ele);
	            
	            ele.addEventListener("mouseout", function(e){
		       		//div1.style.visibility = "hidden";
		       		//div1.style.display = "none";
		       		div1.removeChild1();
		        });
	        });
		};
	
		var singleChkArry1 = ["input", "select", "textarea", "button", "span", "div", "label"];
		for(var i in singleChkArry1){
			var arry = document.getElementsByTagName(singleChkArry1[i]);
			for(var ii = 0 ; ii < arry.length; ii ++){
				var obj = arry[ii];
				if(obj.getAttribute("id") || obj.getAttribute("name")){
					hoverShow(obj, "#FFAC55", getMsg);
				}
			}
		}
		
		//�S�O�B�z Message ----------------------------
		var getMsgFromMap = function(title, rtnMap){
			var msgArry = new Array();
			if(title){
				msgArry.push("<font color='red'>"+title+"</font>");
			}
			var keys = Object.keys(rtnMap).sort();
			var html = "<table style='font-size: 10px;'>"
			var tmpArry = [];
			var tdCount = Math.floor(keys.length / 10);
			tdCount = tdCount == 0 ? 1 : 0;
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
								".showAttr" : { title : "���Init���", 
												dataMap : <c:out value='${mapX120JSON}' default='{}' escapeXml='false'/> 
												},
								".showHidden" : { title : "���Hidden���", 
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

function ${edit_jsp}(){


	<%-- TablE UI���� --%>
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${r"${dispatcher}/${edit_action_clz}/"}');	
	var valid1;
	
	var validAction = {
		// ����ˮ�
		init: function(){
			valid1 = new Validation('form1', {focusOnError:true, immediate:false});
			<#list insertColumns_pk as col>
			valid1.define( "required" , { id : "${col}", errMsg:'${columnLabel[col]}���o����'} );
			</#list>
		},
		
		createRequired : function( type, map ){
			var valid = new Validation('form1', {focusOnError:true, immediate:false});
			Validation.addAllThese([
				['addErrorValid', '', function(v){ return false; } ]
			]);
			for(var key in map){
				switch(type){
					case "required" :
						valid.define( "required" , { id : key, errMsg: map[key] } );
						break;
					default :
						valid.define( "addErrorValid" , { id : key, errMsg: map[key]} );
						break;
				}
			}
			return valid;
		}
	};
	
	
	var actions = {
                
		selectElementResetter : function(selectId, lst, valueKey, textKey, textKey2, defaultVal) {
			if(Array.isArray(lst)){
				this.lst = lst;
				this.length = this.lst.length;
			}else{
				lst = lst || {};
				this.length = Object.keys(lst).length;
				var arry = new Array();
				for(var k in lst){
					arry.push({"key" : k, "val" : lst[k]});
				}
				this.lst = arry;
				valueKey = "key";
				textKey = "val";
				textKey2 = null;
			}
			this.select = $(selectId);
			
			if(this.defaultVal != null && this.defaultVal.length != 0){
				this.select.setAttribute("value", defaultVal);
			}
			
			this.createOption = function(value, text){
				var opt = new Element("option", {value : value});
				opt.update(text);
				if(defaultVal == value){
					opt.setAttribute("selected", "");
				}
				return opt;
			}
			
			$(selectId).length = 0;
			this.select.insert(this.createOption("", "�п��")); 
			
			for(var ii = 0 ; ii < this.lst.length; ii ++){
				var val = this.lst[ii][valueKey];
				var txt = this.lst[ii][textKey];
				var txt2 = this.lst[ii][textKey2];
				if(txt2){
					txt = txt + ":" + txt2;
				}
				var opt = this.createOption(val, txt);
				this.select.insert(opt); 
			}
		},
		
		applyReqMapToForm : function(reqMap){ 
			for(var id in reqMap){
                var value = reqMap[id];
                var element = $(id);
                var spanElement = $(id + "_span");
                if(!element){
                    element = $$("[name="+id+"]");
                    if(element){
                    	element = element[0];
                    }
                }
                if(element){
                    if(element.tagName == "SPAN") {
                    	element.update(value);
                    }else{
                    	element.setValue(value);
                    }
                }
                if(spanElement){
                	spanElement.update(value);
                }
        	}
		},
		
		getValue : function(id) {
			if($(id)){
				return $F(id);
			}
			if($(id + "_span")){
				return $(id + "_span").innerText;
			}
			return "";
		},
		
			
		// �ھ�OP_STATUS�����w���s����
		btnDisable : function(reqMap){
			$w('BTN_confirm  BTN_insert BTN_update BTN_approve BTN_reject BTN_delete  ').each(  //BTN_back
				function(id) { 
					$(id).disable();
				}
			);
			$w('BTN_confirm  BTN_insert BTN_update BTN_approve BTN_delete').each( 
                                function(id) { 
                                        $(id).enable();
                                }
                        );
		},
		
		getreqMap : function (){
                        var sendRecord = {
                        <#list insertColumns as col>
                                <#if col?is_last>
                                        ${col} : actions.getValue('${col}')
                                <#else>
                                        ${col} : actions.getValue('${col}') ,
                                </#if>
                        </#list>
                        }
                        return sendRecord;
                },	
	};
	
	var buttons = {
		<%--����(���N��)--%>
		doIndex_DEP : function(){
			popupWin.popup({									
				src : '${r"${dispatcher}"}/XXZX_0200/prompt' ,		
				scrolling : 'yes',	
				height : '520',
				cb : function(obj){ // �N XXZX_0200�ҿ���� DEP_CODE�N�J�e��DIV_NO , DEP_NM��ܦbDIV_NO��
					$('DIV_NO').setValue(obj.DEP_CODE||'');// ���N��
					$('DEP_NM').update(obj.DEP_NM||'');// ���W��
				}		
			});			
		},
	
		<%--�s�W--%>
		doInsert : function(){
			if(!valid1.validate()){
				return; 
			}
			// �o�e Ajax �ШD
			ajaxRequest.setSynchronous(true); // �}�ҦP�B
			ajaxRequest.post('insert', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
			ajaxRequest.setSynchronous(false); // �}�ҦP�B
		},
		
		<%--�ק�--%>
		doUpdate : function(){
			if(!valid1.validate()){
				return; 
			}
			// �o�e Ajax �ШD
			ajaxRequest.setSynchronous(true); // �}�ҦP�B
			ajaxRequest.post('update', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
			ajaxRequest.setSynchronous(false); // �}�ҦP�B
		},
		
		<%--����--%>
		doSubmit : function(){
			// �o�e Ajax �ШD
			ajaxRequest.setSynchronous(true); // �}�ҦP�B
			ajaxRequest.post('submit', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
			ajaxRequest.setSynchronous(false); // �}�ҦP�B
		},

		<%--�f��--%>
		doApprove : function(){
			// �o�e Ajax �ШD
			ajaxRequest.setSynchronous(true); // �}�ҦP�B
			ajaxRequest.post('approve', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
			ajaxRequest.setSynchronous(false); // �}�ҦP�B
		},
		
		<%--�h�^--%>
		doReject : function(){
			// �o�e Ajax �ШD
			ajaxRequest.setSynchronous(true); // �}�ҦP�B
			ajaxRequest.post('reject', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					actions.btnDisable(resp.rtnMap||{});
				}
			);
			ajaxRequest.setSynchronous(false); // �}�ҦP�B
		},
		
		<%--�R��--%>
		doDelete : function(){
			// �o�e Ajax �ШD
			var isSuccess = false;
			ajaxRequest.setSynchronous(true); // �}�ҦP�B
			ajaxRequest.post('delete', {reqMap : Object.toJSON(actions.getreqMap())},
				function(resp){
					isSuccess = true;
				}
			);
			ajaxRequest.setSynchronous(false); // �����P�B
			
			if(isSuccess){ // �O�_delete���\
				actions.closePopupWin(); // �^�W�@����k
			}
		},
		
		<%--�^�W�@��--%>
		doBack : function(){
			CSRUtil.linkBack(); // �^�W�@����k
		}
	};
	
		
	return {
		initApp : function() {
                        
			<#list insertColumns as col>
			<#if column_dateLst?? && column_timestampLst?? &&
			        (column_dateLst?seq_contains(col) || column_timestampLst?seq_contains(col))>
			<cathay:LocaleDisplay sysCode="RG" dateFields="${col}" var="localeDisplay" />
			</#if>
			</#list>
						
			PageUI.createPageWithAllBodySubElement(
				'${edit_jsp}',
				'�{���}�o�V�mGTU001',
				'���N������GTU001'
			);
			
			// �W�@���^�ӭ��d
			var LP_JSON = CSRUtil.isBackLink('form1');
			if(LP_JSON){
				buttons.doQuery();
			}
			
			var rtnMap = <c:out value='${r"${rtnMap}"}' default='{}' escapeXml='false'/>;  // rtnMap����
			actions.btnDisable(rtnMap);
			actions.applyReqMapToForm(rtnMap);
			validAction.init();
			
			$('BTN_depNm_index').observe('click', buttons.doIndex_DEP); // ����(���N��)
			
			$('BTN_confirm').observe('click', buttons.doConfirm);//����
			$('BTN_back').observe('click', buttons.doBack); // �^�W�@��
			$('BTN_insert').observe('click', buttons.doInsert); // �s�W
			$('BTN_update').observe('click', buttons.doUpdate); // �ק�
			$('BTN_approve').observe('click', buttons.doApprove); // �f��
			$('BTN_reject').observe('click', buttons.doReject); // �h�^
			$('BTN_delete').observe('click', buttons.doDelete); // �R��
			
			// �W�@���^�ӭ��d
                        var LP_JSON = CSRUtil.isBackLink('form1');
                        if(LP_JSON){
                                buttons.doQuery();
                        }
			
			PageUI.resize();
			displayMessage();
		}
	};	
}
	
</script>
</head>
<body>
<form name="form1" id="form1"
	style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px">
<table width="100%" border="0" cellpadding="0" cellspacing="1"
	class="tbBox2">

	<#list 0..formColumns?size-1 as ii>
	<#assign col1=formColumns[ii]!"" />
	<#assign col2=formColumns[ii+1]!"" />
	<tr>
		<td class="tbYellow" width="10%">${columnLabel[col1]!""}</td>
		<td class="tbYellow2" width="40%">
			<input id="${col1!""}" name="${col1!""}" type="text" class="textBox2" />
		</td>
		<td class="tbYellow" width="10%">${columnLabel[col2]!""}</td>
		<td class="tbYellow2" width="40%">
			<input id="${col2!""}" name="${col2!""}" type="text" class="textBox2" />
		</td>
	</tr>
	</#list>
	
	<tr>
		<td class="tbYellow2" colspan="4" align="center">
		<button id="BTN_insert" class="button">�s�W</button>
		<button id="BTN_update" class="button">�ק�</button>
		<button id="BTN_confirm" class="button">����</button>
		<button id="BTN_approve" class="button">�f��</button>
		<button id="BTN_reject" class="button">�h�^</button>
		<button id="BTN_delete" class="button">�R��</button>
		<button id="BTN_back" class="button">�^�W�@��</button>
		</td>
	</tr>

	<tr>
		<td class="tbYellow2" colspan="4" align="center"></td>
	</tr>
</table>
</form>
</body>
</html>