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

