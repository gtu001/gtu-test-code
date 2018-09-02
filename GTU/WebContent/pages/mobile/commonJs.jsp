<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"   %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="jsVer" value="201409031708" />

<script type="text/javascript" src="<c:url value="/mobilestore/js/jquery-1.11.1.min.js"       />?_=${jsVer}" ></script> 
<script type="text/javascript" src="<c:url value="/mobilestore/js/jquery.mobile-1.4.2.min.js" />?_=${jsVer}" ></script>

<script type="text/javascript" src="<c:url value="/mobilestore/js/ajax.js"                    />?_=${jsVer}"></script>
<script type="text/javascript" src="<c:url value="/mobilestore/js/ui.js"                      />?_=${jsVer}"></script>
<script type="text/javascript" src="<c:url value="/mobilestore/js/util.js"                    />?_=${jsVer}"></script>
<script type="text/javascript" src="<c:url value="/mobilestore/js/format.js"                  />?_=${jsVer}"></script>
<script type="text/javascript" src="<c:url value="/mobilestore/js/validation.js"              />?_=${jsVer}"></script>
<script type="text/javascript" src="<c:url value="/mobilestore/js/shoppingGuide.js"           />?_=${jsVer}"></script>

<script type="text/javascript" src="<c:url value="/mobilestore/js/sdc1.js"                    />?_=${jsVer}"></script>
<script type="text/javascript" src="<c:url value="/mobilestore/js/sdc2.js"                    />?_=${jsVer}"></script>
<script type="text/javascript" src="<c:url value="/mobilestore/js/estore-webtrends.js"        />?_=${jsVer}"></script>

<%-- for autocomplete --%>
<script type="text/javascript" src="<c:url value="/mobilestore/js/jquery-ui.min-1.11.1.js"    />?_=${jsVer}" ></script>

<noscript>
	<img alt="" border="0" name="DCSIMG" width="1" height="1" 
		 src="http://weblog.fetnet.net/dcsaeszdi00000g0rd6oxc9q2_9i6m/njs.gif?dcsuri=/nojavascript&amp;WT.js=No" />
</noscript>

<c:remove var="jsVer"/>