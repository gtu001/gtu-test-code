<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>




<c:if test="${not empty lhHandsetGroups}">
 <c:forEach var="item2" items="${lhHandsetGroups}" varStatus="status2" >                	
                    <div class="index_imgBox">
                    	<c:if test="${status2.index == 0 or  status2.index == 2 or status2.index == 4}">
                        <a href="javascript:window.location = '<c:url value="/event?event_id=${lhActivity.id}&co_type=LH&&mobile_web=Y&prod_id=${item2.productId}"/>'">
                            <img src='<c:url value="${item2.img}"/>' />						
							<img src='<c:url value="${item2.secondImage}"/>' />
                        </a>
                        </c:if>
                       <c:if test="${status2.index == 1 or  status2.index == 3 or status2.index == 5}">
                        <a href="javascript:window.location = '<c:url value="/event?event_id=${lhActivity.id}&co_type=LH&mobile_web=Y&prod_id=${item2.productId}"/>'">
                            <img src='<c:url value="${item2.img}"/>' />	
                        </a>
                        </c:if>
                        <div class="text_box">
                            <h2><a href="#">${item2.brandName} ${item2.modelName}</a></h2>
                            <%--  
                            <h3 class="color01">市價 $<span>${status2.index}</span></h3>
                            <h3 class="color02">續約價</h3>
                            --%>
                            <div class="NT">$${item2.lylowestPrice} <a href="javascript:window.location = '<c:url value="/event?event_id=${lhActivity.id}&co_type=LH&&mobile_web=Y&prod_id=${item2.productId}"/>'"></a></div>
                        </div>
                    </div> 
                    
                </c:forEach>
</c:if>



<c:choose>
<c:when test="${fn:length(ActivityDetail) != 0 &&  empty lhHandsetGroups}">
	<c:if test="${'DA_HANDSET' eq coType or 'LH' eq coType}"></c:if>
	<c:forEach var="item" items="${ActivityDetail}" varStatus="status" >
		<div class="index_imgBox">
		<c:if test="${status.index == 0 or  status.index == 2 or status.index == 4}">
			<a href="#">
				<img src='<c:url value="${item.defaultImage}"/>' />						
				<img src='<c:url value="${item.secondImage}"/>' />
			</a>
		</c:if>
        <c:if test="${status.index == 1 or  status.index == 3 or status.index == 5}">
	         <a href="#">
	             <img src='<c:url value="${item.defaultImage}"/>' />	
	         </a>
        </c:if>
        <div class="text_box">
            <h2><a href="#">${item.viewDesc}</a></h2>
            <h3 class="color01">市價 $<span>${status.index}</span></h3>
            <h3 class="color02">續約價</h3>
            <div class="NT">${status.index} 起 <a href="#"></a></div>
        </div>
    </div> 
    </c:forEach>
</c:when>
<c:otherwise>
	<div id="noData"></div>
</c:otherwise>
</c:choose>