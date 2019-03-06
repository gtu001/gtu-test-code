
function paginationInit(paginationAreaJSON, formQry, pageNoQry, pageSizeQry){
	pageNoQry = pageNoQry || "input[id='pageNo']";
	pageSizeQry = pageSizeQry || "input[id='pageSize']";
	formQry = formQry || "#form1";
	
	var paginationObj = JSON.parse(paginationAreaJSON);/*[[${paginationAreaJSON}]]*/
	
	if(!paginationObj){
		$('#pagination-contairner').hide();
	} else {
		paginationObj = $.extend(paginationObj, {
		});
		$('#pagination-container').pagination(paginationObj);
		$("li.paginationjs-page a").click(function(){
			var pageNum = $(this).parent().attr("data-num");
			
			$(pageNoQry).val(pageNum);
			$(pageSizeQry).val(10);
			
			$(formQry).submit();			        	
		});
		$("input.J-paginationjs-go-button").click(function(){
			var pageNum = $("input.J-paginationjs-go-pagenumber").val();
			
			$(pageNoQry).val(pageNum);
			$(pageSizeQry).val(10);
			
			$(formQry).submit();	
		});
		if(paginationObj.dataSource.length == 0){
			$('#pagination-contairner').hide();
		}
	}
}