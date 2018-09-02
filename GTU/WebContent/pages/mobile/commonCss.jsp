<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"   %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="cssVer" value="201408221415" />

<link href="<c:url value="/mobilestore/css/jquery.mobile-1.4.2.css"   />?_=${cssVer}" rel="stylesheet" />
<link href="<c:url value="/mobilestore/css/main.css"                  />?_=${cssVer}" rel="stylesheet" />
<link href="<c:url value="/mobilestore/owl-carousel/owl.carousel.css" />?_=${cssVer}" rel="stylesheet" />
<link href="<c:url value="/mobilestore/owl-carousel/owl.theme.css"    />?_=${cssVer}" rel="stylesheet" />

<%-- for autocomplete --%>
<link href="<c:url value="/mobilestore/css/jquery-ui.css"             />?_=${cssVer}" rel="stylesheet" />

<c:remove var="cssVer"/>


<style type="text/css">
	.ui-loader-background {
	    width:100%;
	    height:100%;
	    top:0;
	    margin: 0;
	    background: rgba(0, 0, 0, 0.3);
	    display:none;
	    position: fixed;
	    z-index:100;
	}
	
	.ui-loading .ui-loader-background {
	    display:block;
	}
	
	<%-- shoppingguide accessory select --%>
	.select_n .ui-btn {
		margin-top: -25px;
		padding-right: 0px;
	}
</style>
