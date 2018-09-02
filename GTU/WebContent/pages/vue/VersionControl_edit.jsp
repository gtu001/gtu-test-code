<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@ page import="com.fuco.mb.bill.dto.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.fuco.mb.conv.bank.writer.*"%>
<%@ page import="java.net.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/Skin2.css" />

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css" />
<!-- <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css" /> -->
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/bill-form.css" />
<style>
.ck-editor__editable {
    min-height: 400px;
}
</style>

<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>

<script src="<%=request.getContextPath()%>/js/bill-form.js"></script>
<script	src="<%=request.getContextPath()%>/js/jquery.stickytableheaders.min.js"></script>
<!-- <script src="<%=request.getContextPath()%>/js/ckeditor.js"></script>-->
<script src="<%=request.getContextPath()%>/js/tinymce/tinymce.min.js"></script>
<script src="js/vue.min.js"></script>

<script type="text/javascript">
	var editor = null;

	$(document).ready(function(){
		/*
	     ClassicEditor
		.create( document.querySelector( '#editor' ), {
		    toolbar: [ 'headings', 'bold', 'italic', 'link', 'bulletedList', 'numberedList', 'blockQuote' ],
		    heading: {
		        options: [
		            { modelElement: 'paragraph', title: 'Paragraph', class: 'ck-heading_paragraph' },
		            { modelElement: 'heading1', viewElement: 'h1', title: 'Heading 1', class: 'ck-heading_heading1' },
		            { modelElement: 'heading2', viewElement: 'h2', title: 'Heading 2', class: 'ck-heading_heading2' },
		            { modelElement: 'heading3', viewElement: 'h3', title: 'Heading 3', class: 'ck-heading_heading3' }
		        ]
		    },
		} )
		.then( editorInner => {
		    console.log( editorInner );
		    editor = editorInner;
		} )
		.catch( error => {
		    console.log( error );
		} );
		*/
		
		tinymce.init({
			 language:'zh_TW',
			 selector:'#editor',
			 height:'460',
			    plugins: [
			        "advlist autolink lists link image charmap print preview hr anchor pagebreak",
			        "searchreplace wordcount visualblocks visualchars code fullscreen",
			        "insertdatetime media nonbreaking save table contextmenu directionality",
			        "emoticons template paste textcolor colorpicker textpattern imagetools"
			    ],
			    toolbar1: "insertfile undo redo | formatselect fontselect fontsizeselect | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | table hr pagebreak blockquote",
			    toolbar2: "bold italic underline strikethrough subscript superscript | forecolor backcolor charmap emoticons | link unlink image media | cut copy paste | insertdatetime fullscreen code | checkbox radio newBtn1 newBtn2 ",
			    menubar: false,
			 image_advtab: true,
			 setup: function (editor) {
			    editor.addButton('checkbox', {
			      text: '',
			      tooltip: 'Checkbox',
			      image: tinymce.baseURL + '/plugins/custom/img/checkbox.jpg',
			      icon: false,
			      onclick: function () {
			        editor.insertContent('<input type="checkbox" />');
			      }
			    });
			    editor.addButton('radio', {
			      text: '',
			      tooltip: 'Radio',
			      image: tinymce.baseURL + '/plugins/custom/img/radio.jpg',
			      icon: false,
			      onclick: function () {
			        editor.insertContent('<input type="radio" />');
			      }
			    });
			  },
		});
		 
			  
		$("#btn1").click(function(){
			//showInPopupWindow(320, 568);
			openWindowWithPostRequest(320, 568);
		});
		$("#btn2").click(function(){
			//showInPopupWindow(0, 0);
			openWindowWithPostRequest(0, 0);
		});
	});
	
	function getContent(){
		//return editor.getData();
		var str = tinymce.activeEditor.getContent();
		return str;
	}
	
	var win = null;
	function showInPopupWindow(width, height){
		var content = getContent();
		if(win != null && win != undefined){
			win.close();
		}
		win = window.open('versionControl_pupop','Popup_Window', //
			'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=0,width=500,height=300,left = 312,top = 234');
		win.focus();
		$(win.document).ready(function() {
			if(width == 0 && height == 0){
				win.showData4Window(content);
			}else{
				win.showData4Mobile(content, width, height);
			}
		});
	}

	function openWindowWithPostRequest(width, height) {
		var winName='MyWindow';
		var winURL='versionControl_pupop';
		var windowoption='resizable=yes,height=600,width=800,location=0,menubar=0,scrollbars=1';
		var params = { 'content' : getContent(), "width" : width, "height" : height};         
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", winURL);
		form.setAttribute("target",winName);  
		for (var i in params) {
			if (params.hasOwnProperty(i)) {
				var input = document.createElement('input');
				input.type = 'hidden';
				input.name = i;
				input.value = encodeURIComponent(params[i]);
				form.appendChild(input);
			}
		}              
		document.body.appendChild(form);                       
		var win = window.open('', winName,windowoption);
		win.focus();
		form.target = winName;
		form.submit();                 
		document.body.removeChild(form);           
	}
        
</script>
</head>
<body>
	<div id="app">
	
		<h1>{{getTitle()}}</h1>
		
		<form id="form">
        
                <textarea name="content" id="editor">
                </textarea>
                <br />
                	版本:<input type="text" v-model="version" v-bind:value="version" /><font color="red">＊</font>
                <br />
                <br />
                <input type="button" id="btn1" value="手機顯示" />
                <input type="button" id="btn2" value="網頁顯示" />
                <input type="button" id="btn3" value="儲存" v-on:click="confirmBtn" />
	
        </form>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		var type = "<%=request.getAttribute("type")%>";
		app.type = type;
	});

	var app = new Vue({
		el : '#app',
		data : {
			version : "",
			type : "",
		},
		methods : {
			getTitle: function(){
				switch(this.type){
				case "1":
					return "上傳分期總約書";
				case "2":
					return "上傳申請注意事項文字";
				} 
				return "unknow";
			},
			confirmBtn : function(e) {
				$(e.target).blur();
				
				var content = getContent();
				if($.trim(content) == ''){
					alert("編輯內容不可為空白");
					return;
				}
				if($.trim(this.version) == ''){
					alert("版本不可為空白");
					return;
				}
				
				var data = this.$data;
				$.extend(data, {content : content});

				$.ajax({
					type : 'POST',
					url : "versionControl_editConfirm",
					data : data,
					dataType : "JSON",
					success : function(data) {
						console.log('版本更新完成');
						if (data && data.result == true) {
							alert('版本更新完成');
							document.location.href = "versionControl_query?type=" + app.type;
						} else if (data && $.trim(data.message) != '') {
							alert(data.message);
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log("版本更新失敗");
						alert('版本更新失敗！！，請聯繫系統管理人員處理，謝謝');
						//alert(jqXHR + " , " + textStatus + " , " + errorThrown);
					}
				});
			},
		}
	});
</script>
</html>