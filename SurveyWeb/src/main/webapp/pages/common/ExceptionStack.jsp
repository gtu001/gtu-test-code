<a id="exceptionStackLink" href="javascript:showExceptionStackArea();" style="color:#FFFFFF">.</a>
<br/>
<textarea id="exceptionStackArea" rows="50" style="width:100%;display:none">
  <%if(request.getAttribute("exceptionStack")!=null){
		out.println(request.getAttribute("exceptionStack"));
	}%>
</textarea>
<script type="text/javascript">
	$(function(){
		if($.trim(new String($("#exceptionStackArea").val())).length == 0){
			$("#exceptionStackLink").hide();
		}
	})
	function showExceptionStackArea(){
		$("#exceptionStackArea").toggle();
	}
</script>
<%-- <%@ include file="/ls/arap/cash/common/ExceptionStack.jsp"%> --%>