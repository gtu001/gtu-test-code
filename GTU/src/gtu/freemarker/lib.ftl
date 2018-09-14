<#macro copyright date> 
 <p>Copyright (C) ${date} Julia Smith. All rights reserved. 
  <br>Email: ${mail}</p> 
</#macro> 
<#assign mail = "jsmith@acme.com"> 


<#function libfunc val>
        <#return "libfunc = " + val>
</#function>


<#setting date_format="yyyyMMdd">
<#setting time_format="HHmmss">
<#setting datetime_format="yyyyMMddHHmmss">
<#macro mingoDate date>民國${date[0..3]?number-1911}年${date[4..5]?number}月${date[6..7]?number}日</#macro>

<#-- 民國年 -->
<#function mingoDate1 date>
        <#if date?is_date>
                <#local nd=date?date>
        <#elseif date?is_string && date?matches('\\d{8}')>
                <#local nd=date>
        <#else>
                <#return "error_date">
        </#if>
        <#local year=nd[0..3]?number-1911>
        <#local month=nd[4..5]?number>
        <#local dddd=nd[6..7]?number>
        <#return "${year}年${month}月${dddd}日">
</#function>

<#-- 西元年 -->
<#function ciDate1 date>
        <#if date?is_date>
                <#local nd=date?date>
        <#elseif date?is_string && date?matches('\\d{8}')>
                <#local nd=date>
        <#else>
                <#return "error_date">
        </#if>
        <#local year=nd[0..3]?number?c>
        <#local month=nd[4..5]?number>
        <#local dddd=nd[6..7]?number>
        <#return "${year}/${month}/${dddd}">
</#function>

<#-- 陣列串接 -->
<#function listJoin list delimit>
        <#local rtn = "">
        <#list list as x>
                <#if x?is_last>
                        <#local rtn = rtn + x + ''>
                <#else>
                        <#local rtn = rtn + x + delimit>
                </#if>
        </#list>
        <#return rtn>
</#function>

<#-- 建立新陣列 -->
<#function fixArry list prefix suffix isCapitalize>
	<#local lst = []>
	<#list list as x>
	        <#local x2 = x>
	        <#if isCapitalize>
	                <#local x2 = x?capitalize>
	        </#if>
		<#local lst = lst + [prefix + x2 + suffix]>
	</#list>
	<#return lst>
</#function>