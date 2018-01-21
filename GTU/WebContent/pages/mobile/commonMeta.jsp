<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"   %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>   

<meta http-equiv="Content-Type"       content="text/html; charset=utf-8" />
<meta http-equiv="Content-Style-Type" content="text/css"/>  
<meta http-equiv="Cache-Control"      content="no-store" />
<meta http-equiv="Pragma"             content="no-cache" />
<meta http-equiv="Expires"            content="0" />	

<%-- can be search/index for robot, if in production env. --%>
<c:set var="mobileStaging"><bean:message key="mobile.staging" bundle="system" /></c:set>
<c:if test="${mobileStaging != 'production' }">
	<META NAME="GOOGLEBOT" CONTENT="NOSNIPPET">
	<META NAME="ROBOTS"    CONTENT="NOARCHIVE">
</c:if>

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<title>遠傳 fetnet</title>


<%-- Webtrends Flow --%>
<c:if test="${not empty headerMetaWtSin}"><meta name="WT.si_n" content="${headerMetaWtSin}" /></c:if>
<c:if test="${not empty headerMetaWtSip}"><meta name="WT.si_p" content="${headerMetaWtSip}" /></c:if>

<%-- WebTrends CSPUser --%>
<c:if test="${headerMetaWtHasCSP}">
	<meta name="WT.dcsvid" content="${headerMetaWtDcsvid}" />
	<meta name="WT.seg_1"  content="${headerMetaWtSeg1}"   />
	<meta name="WT.seg_2"  content="${headerMetaWtSeg2}"   />
	<meta name="WT.seg_3"  content="${headerMetaWtSeg3}"   />
</c:if>

<%-- WebTrends Billing --%>
<c:if test="${headerMetaWtDone}">
	<meta name="WT.pc"  content="${headerMetaWtPncg}"  />
	<meta name="WT.tx_e"   content="${headerMetaWtTxe}"   />
	<meta name="WT.pn_sku" content="${headerMetaWtPnSku}" />
	<%-- <meta name="WT.pn"     content="${headerMetaWtPn}"    />--%>
	<meta name="WT.tx_t"   content="${headerMetaWtTxt}"   />
	<meta name="WT.tx_u"   content="${headerMetaWtTxu}"   />
	<meta name="WT.tx_s"   content="${headerMetaWtTxs}"   />	
	<meta name="WT.tx_i"   content="${headerMetaWtTxi}"   />
	<meta name="WT.tx_id"  content="${headerMetaWtTxid}"  />
	<meta name="WT.tx_it"  content="${headerMetaWtTxit}"  />
</c:if>			