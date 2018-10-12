<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<%@ page language='java' contentType='text/html; charset=BIG5'%>

<!--
�{���GXXZX0100.jsp
�\��G��EBAF�m��
�@�̡G
�����G
-->

<html>
<head>
<%@ include file='/html/CM/header.jsp'%>
<%@ include file='/html/CM/msgDisplayer.jsp'%>
<%@ taglib prefix='im' tagdir='/WEB-INF/tags/im'%>
<!--�פJ�~��Javascript �P css-->

<title></title>
<link href='${cssBase}/cm.css' rel='stylesheet' type='text/css'>
<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/prototype.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ajax/CSRUtil.js'></script>
<!-- ���n -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/TableUI.js'></script>
<!-- �ثe�t�α`�Ϊ����u�� -->
<script type='text/JavaScript' src='${htmlBase}/CM/js/ui/PageUI.js'></script>
<script type='text/javaScript' src='${htmlBase}/CM/js/ui/LocaleDisplay.js'></script>
<script type="text/JavaScript" src="${htmlBase}/CM/js/RPTUtil.js"></script>
<script type='text/javascript' src='${htmlBase}/CM/js/ui/commView.js'></script>
<script type="text/JavaScript" src="${htmlBase}/CM/js/calendar.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/Validator.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/ui/core.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/jsonUtil.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/utility.js"></script>
<script type="text/javascript" src="<%=htmlBase%>/CM/js/ui/popupWin.js"></script>
<script type='text/javascript'>

<%-- ���͵e������ --%>
<%--�z�L prototype �� Event �����ť onload �ƥ�AĲ�o�ɶi�� initApp()--%>
Event.observe(window, 'load', new LSM01000().initApp);

function LSM01000(){
//LSM0_0900 >> ��W����((������ˮֲ��`��,�ץX,���

	<%-- TablE UI���� --%>
	var valid1;
	var grid;
	var ajaxRequest = new CSRUtil.AjaxHandler.request('${dispatcher}/XXZX_0100/');	

	<%--���s���v--%>
	function buttonAuth() {
	
    }
	

	<%--�e���ˮ�--%>
	function initValidation(){
		
	}
	<%--����ˮ�--%>
	function defineValid(isQuery){		   
		
	}
	
	
	<%-- �s���e��--%>
	function getreqMap(){

	};



	<%--�\����s--%>
	var buttons = {

		<%-- �d�� --%>
		doQuery : function(){
			
		},		
		<%--�o�e�ꤺ�Ѳ������O��ʰT���q��--%>	
		 doSendMail : function(){		   	    
            
		},

		<%--�פJ--%>					
		doImport : function() {
			
		},
		<%-- �N������ --%>
		doIndex:function(){
			
		},
		
		<%-- �M�� --%>
		doClear : function(){

		}
		

	};	
	function changeSNO_TYPE(event){
		var radio = event.element();
		$('SNO').disabled= (radio.value!=1) ;
	};
	function changeMKT_CODE_TYPE(event){
			var radio = event.element();
	
		// �����O MKT_CODE_TYPE 1.�w�]�����������O
		//               2.�����@�����O�ɡAenable�u�����O�v�U�Կ��
		//               3.������զX�h��ɡAopen�u�����O�v�h�����
			if(radio.value==0) {
		
			// 2.4.1 ���զX������ɡAPFL_ID =�u���զX�v�U�Կ�檺�Ҧ��ﶵ(�r�I�j�})	
			var selectValue = '';	
			
			for(i=0;i<$('MKT_CODE').options.length;i++) {
			    if($('MKT_CODE').options[i].value!='') {
					selectValue +=  $('MKT_CODE').options[i].value + ",";	
				}
			}
			if(selectValue.length > 0){
				selectValue = selectValue.substring(0, selectValue.length - 1);
			}	
		    $('theMKT_CODE_List').value=selectValue;
		    $('theMKT_CODE_List_TEXT').value=selectValue;		
		    
		    
		} else if(radio.value==1) {
		} else if(radio.value==2) {	
			
			//�}�Ҹ�Ʀh�����
			popupWin.popup({
				src: '<%=dispatcher%>/LSM0_1000/promptPFL',
				width:600,       	// �e default 800px
				height:210 ,		// �� default 500px
				left:30,			// ������Z default 30px
				top:30,			// �W����Z default 50px
				scroll:'yes',
				cb:function(obj){
					    var selectValue = '';	
						for(var i=0; i<obj.length; i++){
							var m = obj[i];
							//alert( m['DEFAULT_NAME'] );
							//alert( m['DEFAULT_VALUE']);	
							selectValue = selectValue + m['DEFAULT_VALUE'] + ",";	
						}
						if(selectValue.length > 0){
							selectValue = selectValue.substring(0, selectValue.length - 1);
						}						
 					    $('theMKT_CODE_List').value=selectValue;
 					    $('theMKT_CODE_List_TEXT').value=selectValue;
				}
			}); 
		};
	
		$('MKT_CODE').disabled= (radio.value!=1) ;
	    $('theMKT_CODE_List_TEXT').disabled = (radio.value!=0 && radio.value!=2) ;
	};
	
	return {			
		initApp : function() {			
			PageUI.createPageWithAllBodySubElement("LSM01000","�ˮ֧@�~","�Ѳ����L���ˮ�");
			<cathay:LocaleDisplay sysCode="LS" dateFields = "SYS_DT_STR SYS_DT_END"  var="localeDisplay"/>
			displayMessage();
			grid = new TableUI({
				table :$('grid'),
				autoCheckBox:{isSelectAll:true},
				validateRecord: function(rec, sn){ return rec.PRICE_CHECK_RESULT == 'N'; },
            	split:['ACC_CLOSE_PRICE'],
				pageSize :10,
            	whenDataNull:"",
				column:[	
					{header: "�w�s���" , key:'BAL_DATE', sortable:true, sortRule:'date', attrs:{rowSpan:2}}  
					,{header: "����" , key:'BAL_TYPE_NAME', attrs:{rowSpan:2}}
					,{header: "�����N�X" , key:'BAL_TYPE', attrs:{rowSpan:2}}
					,{header: "�Ѳ��N��(�~�X)" , key:'STK_NO', sortable:true, attrs:{rowSpan:2}}
					,{header: "�Ѳ��N��(���X)" , key:'SNO', attrs:{rowSpan:2}}
					,{header: "�Ѳ��W��" , key:'STK_SNAME', sortable:true, attrs:{rowSpan:2}}
					,{header: "�����O" , key:'MKT_CODE', sortable:true, attrs:{rowSpan:2}}
					,{header: "�����O�N�X" , key:'MKT_CODE_NAME', attrs:{rowSpan:2}}
					,{header: "�ˮֵ��G" , key:'PRICE_CHECK_RESULT', sortable:true, attrs:{rowSpan:2}}
					,{header: "���q�w�s" , attrs:{colSpan:2} , empty:'rowSpan_down'}
					,{header: "NAV�w�s" , attrs:{colSpan:2} , empty:'rowSpan_down'}
					,{header: "�_�Ӯw�s" , attrs:{colSpan:2} , empty:'rowSpan_down'}�@
					,{header: "Cmoney" , attrs:{colSpan:2} , empty:'rowSpan_down'}
					,{header: "��������" , attrs:{colSpan:2} , empty:'rowSpan_down'}
					,{header: "�Ƶ�" , key:'REMARK', attrs:{rowSpan:2}}
					,{header: "���ʤH��ID" , key:'LST_PROC_ID', attrs:{rowSpan:2}}
					,{header: "���ʤH���m�W" , key:'LST_PROC_NAME', attrs:{rowSpan:2}}
					,{header: "���ʮɶ�" , key:'LST_PROC_DATE', attrs:{rowSpan:2}}
					,{header: "�t�ΧO" , key:'SYS_NO', attrs:{rowSpan:2}} 
					
					,{header: "���L��" , key:'ACC_CLOSE_PRICE', sortable:true }
					,{header: "�ѻ����" , key:'ACC_MP_DATE', sortRule:'date', sortable:true }
					,{header: "���L��" , key:'PFL_CLOSE_PRICE', sortable:true }
					,{header: "�ѻ����" , key:'PFL_MP_DATE', sortRule:'date', sortable:true }
					,{header: "���L��" , key:'SRC3_CLOSE_PRICE', sortable:true }
					,{header: "�ѻ����" , key:'SRC3_MP_DATE', sortRule:'date', sortable:true }
					,{header: "���L��" , key:'CMONEY_CLOSE_PRICE', sortable:true }
					,{header: "�ѻ����" , key:'CMONEY_MP_DATE', sortRule:'date', sortable:true }
					,{header: "���L��" , key:'SRC7_CLOSE_PRICE', sortable:true }
					,{header: "�ѻ����" , key:'SRC7_MP_DATE', sortRule:'date', sortable:true }
				]}
			);
			initValidation();
			buttonAuth();    <%-- ���s�v�� --%>
			

			$('btn_query').observe('click', buttons.doQuery);<%-- �d�� --%>
			$$('input[name="SNO_TYPE"]').each(function(rad){
				rad.observe('click', changeSNO_TYPE);
			});	
			$$('input[name="MKT_CODE_TYPE"]').each(function(rad){
				rad.observe('click', changeMKT_CODE_TYPE);
			});
			
			
						
			<%--�W�@���^�ӭ��d--%>
			if(CSRUtil.isBackLink('form1')){
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
<form name='form1' id='form1' style='PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px'>
<table width='100%' border='0' cellpadding='0' cellspacing='1' class='tbBox2' id="table1">
	<tr>
		<td class='tbYellow' width="20%">�w�s���</td>
		<td class='tbYellow2'>
			<input id="SYS_DT_STR" name="SYS_DT_STR" type="text" class="textBox2" value="${SYS_DATE}" /> ~ 
			<input id="SYS_DT_END" name="SYS_DT_END" type="text" class="textBox2" value="${SYS_DATE}" />�@�@�@�@�@�@
			<label><input type="checkbox" name="IS_CHECK" id="IS_CHECK" value="1" checked /> ������ˮֲ��`��</label>	
		</td>
		<td class="tbYellow2" align="center" rowspan='4' width="20%">
				<button class="button" id="BTN_QUERY" >�d��</button> &nbsp;
				<button class="button" id="BTN_CLEAR" >�M��</button>
		</td>
	</tr>
	<tr>
		<td class="tbYellow">�w�s����</td>
		<td class="tbYellow2" >
			<select id="BAL_TYPE" name="BAL_TYPE">
				<option value="0" >�鵲</option>
				<c:forEach var="map" items="${BAL_TYPE_List}" >
					<option value = "${map.CODE}" >${map.CODE}</option>
				</c:forEach>
			</select>		
		</td>
	</tr>
	<tr>
		<td class="tbYellow">
			<input type="radio" name="SNO_TYPE" id="SNO_TYPE_0" value="0" checked />�����Ѳ�
			<input type="radio" name="SNO_TYPE" id="SNO_TYPE_1" value="1" />��@�Ѳ�
		</td>
		<td class='tbYellow2''>
			<select id="SNO" style="width:160px" class="textBox2" disabled>
				<option value="">����</option>
				<c:forEach var="map" items="${theSTK_List}">
					<option value = "${map.SNO}">${map.STK_NO} ${map.STK_SNAME}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td width="10%" class="tbYellow">
		    <input type="radio" name="MKT_CODE_TYPE" id="MKT_CODE_TYPE_0"  value="0" checked>���������O
		    <input type="radio" name="MKT_CODE_TYPE" id="MKT_CODE_TYPE_1"  value="1">��@�����O
		    <input type="radio" name="MKT_CODE_TYPE" id="MKT_CODE_TYPE_2"  value="2">�����O�h��
		</td>	
		<td class="tbYellow2">		
			<select id="MKT_CODE" name="MKT_CODE" class="textBox2" style="width:160px" disabled>
				<option value="">����</option>
				<c:forEach  var="map" items="${MKT_CODE_List}">
					<option value="<c:out value="${map.CODE}" />" ><c:out value="${map.CODE}" /></option>
				</c:forEach>
			</select> &nbsp;			    
			<input type="text" name="theMKT_CODE_List_TEXT" id="theMKT_CODE_List_TEXT" value="" readOnly class="textBox2" size="50" disabled>
			<input type="text" id="theMKT_CODE_List" />
		</td>	
	</tr>
	<tr>
		<td align="center" class="tbYellow2" colspan='3'>
			<button id="BTN_REMARK" class="button" >�Ƶ����@</button> &nbsp;	
			<button id="BTN_EXPORT" class="button" >�ץX</button>
		</td>
	</tr>
</table>
</form>
<table id='grid'></table>
</body>
</html>