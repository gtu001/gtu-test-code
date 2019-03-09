------------------------------------------------------------------------------------

未定義的作法

${use_key!'use_key_not_found_show'}

------------------------------------------------------------------------------------


<#if arry[3]?lower_case == 'null'>
	reqMap.put("${arry[2]}", null); //${arry[1]}
<#elseif arry[3]?lower_case?matches("\-?\d+(|\.\d+)")>   
	reqMap.put("${arry[2]}", ${arry[3]}); //${arry[1]}
<#else>
	reqMap.put("${arry[2]}", "${arry[3]}"); //${arry[1]}
</#if>

------------------------------------------------------------------------------------
正則表達示  
	
	${strValue?matches("\\-?\\d+(|\\.\\d+)")}
	
	Ps : 斜線要兩個


------------------------------------------------------------------------------------


message =  ${message}
user = ${user}

user ###
${"Hello ${user}!"} 
${"${user}${user}${user}${user}"} 
列印第一個字元到第四個=${user[1..4]}
user ###

latestProduct.url0 = ${latestProduct.url}
latestProduct.name0 = ${latestProduct.name}
latestProduct.url1 = ${latestProduct["url"]}
latestProduct.name1 = ${latestProduct["name"]}


## 字串轉數值 ##
${"12345678"?number}




## if ##
<#if user == "Big Joe">
        find Big Joe!!
<#elseif user == "AAAA">
        find AAAA
<#else>
        find ELSE
</#if>

<#if user1 == "Big Joe">
        find Big Joe!!
<#elseif user1 == "AAAA">
        find AAAA
<#else>
        find ELSE
</#if>

<#if user2 == "Big Joe">
        find Big Joe!!
<#elseif user2 == "AAAA">
        find AAAA
<#else>
        find ELSE
</#if>





## comment 註解 ##
[[[xxxx1
<#-- ${testComment} -->
xxxx2]]]





array = ${array[0].val}

array[0].seq = ${array[0].seq}
必須加括號否則右邊角括號會被當作判斷式結尾
<#if (array[0].seq > 10)>
        big 10
<#elseif array[0].seq < 5>
        smail 5
<#else>
        other num
</#if>





######
可用的函式?xxxxx
chunk,  is_date,  is_hash,  float,  matches,  time,  number_to_datetime,  byte,  substring,  is_transform,  
web_safe,  iso_ms_nz,  groups,  seq_contains,  iso,  is_method,  eval,  iso_ms,  iso_utc_m_nz,  parent,  capitalize,  
number,  if_exists,  is_directive,  iso_utc_h_nz,  floor,  is_boolean,  split,  node_name,  seq_index_of,  is_sequence,  
sort,  is_node,  sort_by,  xhtml,  iso_local_m_nz,  iso_nz,  node_namespace,  date,  last_index_of,  short,  ancestors,  
length,  datetime,  iso_local_ms,  json_string,  reverse,  c,  keys,  iso_local_h_nz,  has_content,  replace,  is_hash_ex,  
is_number,  new,  lower_case,  string,  exists,  last,  root,  j_string,  contains,  round,  long,  ends_with,  number_to_date,  
namespace,  values,  seq_last_index_of,  uncap_first,  iso_local,  is_macro,  index_of,  word_list,  int,  iso_utc_nz,  xml,  
double,  node_type,  rtf,  url,  iso_m,  size,  default,  iso_h,  iso_utc_m,  ceiling,  iso_h_nz,  is_enumerable,  iso_utc_h,  
iso_local_nz,  iso_m_nz,  left_pad,  iso_utc_ms_nz,  cap_first,  interpret,  children,  chop_linebreak,  is_collection,  iso_utc,  
trim,  is_string,  number_to_time,  upper_case,  js_string,  right_pad,  is_indexable,  iso_local_ms_nz,  iso_utc_ms,  iso_local_h,  
html,  iso_local_m,  first,  starts_with 
######
    <ATTEMPT> ...
    <IF> ...
    <ELSE_IF> ...
    <LIST> ...
    <FOREACH> ...
    <SWITCH> ...
    <ASSIGN> ...
    <GLOBALASSIGN> ...
    <LOCALASSIGN> ...
    <_INCLUDE> ...
    <IMPORT> ...
    <FUNCTION> ...
    <MACRO> ...
    <TRANSFORM> ...
    <VISIT> ...
    <STOP> ...
    <RETURN> ...
    <CALL> ...
    <SETTING> ...
    <COMPRESS> ...
    <COMMENT> ...
    <TERSE_COMMENT> ...
    <NOPARSE> ...
    <END_IF> ...
    <ELSE> ...
    <BREAK> ...
    <SIMPLE_RETURN> ...
    <HALT> ...
    <FLUSH> ...
    <TRIM> ...
    <LTRIM> ...
    <RTRIM> ...
    <NOTRIM> ...
    <SIMPLE_NESTED> ...
    <NESTED> ...
    <SIMPLE_RECURSE> ...
    <RECURSE> ...
    <FALLBACK> ...
    <ESCAPE> ...
    <NOESCAPE> ...
    <UNIFIED_CALL> ...
    <WHITESPACE> ...
    <PRINTABLE_CHARS> ...
    <FALSE_ALERT> ...
######





#array0
<#list array[0..1] as var> 
        [[${var.val} -- ${var.seq}]] || ${var.val?length} || ${var.val?substring(0, 1)} ||
        <#if (array[0].val == "")>
                val is empty
        </#if>
</#list> 
#array1
<#list array[1..] as var> 
        [[${var.val} -- ${var.seq}]]
</#list> 


## list 取得index --> freemarker 2.3.23 才有
<#list ['a', 'b', 'c'] as x>
  ${x?index}
  <#if x?index == list?size - 1>
  	此為最後一筆
  </#if>
  <#if x?is_last == true>
  	此為最後一筆
  </#if>
  	${x?has_next}
	${x?index}
	${x?is_even_item}
	${x?is_first}
	${x?is_last}
	${x?is_odd_item}
</#list>




## 未設值時付值判斷 ##
no set value use default = [[${no_set_this_attribute!"this is default value"}]]

如果參數no_set_this_attribute不存在，則if中間那段不會顯示
<#if no_set_this_attribute?>Never show this message!!</#if>






${"It's \"quoted\" and 
this is a backslash: \\"}





列印特殊符號 Escape 方法
${r"${foo}"} 
${r"C:\foo\bar"} 




## list ##
<#list ["winter", "spring", "summer", "autumn"] as x> 
${x} 
</#list> 

<#list ["Joe", "Fred"] + ["Julia", "Kate"] as user> 
- ${user} 
</#list> 


## list包Map ## ==> [{"key_str":"value1"}, {"key_str":"value2"}]
<#list list1 as x> 
	${x["key_str"]} 
</#list> 


after user : ${user}






<#assign ages = {"Joe":23, "Fred":25} + {"Joe":30, "Julia":18}>
- Joe is ${ages.Joe} 
- Fred is ${ages.Fred} 
- Julia is ${ages.Julia} 





${1.999?int}  轉型int





## 字串測試 ##
html = ${"<>\"'&"?html} [escape html]
cap_first = ${"aaaaaa"?cap_first} [首字大寫]
lower_case = ${"aaaaaa"?lower_case} 
upper_case = ${"aaaaaa"?upper_case} 
trim = [${"  AAAA BBB   "?trim}]
<#-- size = ${"1234567"?size} 不知為何有錯 -->
<#-- int = ${"1234567.123"?int} 不知為何有錯 -->
複合 = ${"<johny walker>"?upper_case?html}






### escape start ##
<#escape x as x?html> 
  ... 
  <p>Title: ${book.title}</p> 
  <p>Description: 
<#noescape>${book.description}</#noescape></p> 
  <h2>Comments:</h2> 
  <#list book.comment as comment> 
    <div class="comment"> 
      ${comment} 
    </div> 
  </#list> 
  ... 
</#escape> 
### escape end ##





## setting command ##
<#--
<#setting number_format="###.###.###">
<#setting boolean_format="是,否">
失敗 以後再研究-->

before setting
date = ${date?date}
date = ${date?time}
date = ${date?datetime}
<#setting date_format="yyyy/MM/dd">
<#setting time_format="HH:mm:ss">
<#setting datetime_format="yyyy/MM/dd HH:mm:ss">
after setting
date = ${date?date}
date = ${date?time}
date = ${date?datetime}
## setting command ##





##marco test#####################################################################
<#macro greet person color="red"> 
  <font size="+2" color="${color}">Hello ${person}!</font> 
</#macro> 
<@greet person="Fred" color="black"/> 
<@greet color="black" person="Fred"/> 
<@greet person="John"/> 


<#macro border> 
  <table border=4 cellspacing=0 cellpadding=4><tr><td> 
    <#nested> 
    <#nested> 
  </td></tr></table> 
</#macro> 
<@border>The bordered text</@border> 


<#macro repeat count> 
  <#local y = "test"> 
  <#list 1..count as x> 
    ${y} ${count}/${x}: <#nested> 
  </#list> 
</#macro> 
macro的局部變量是不可見的
<@repeat count=3>${y!"?"} ${x!"?"} ${count!"?"}</@repeat> 


<#macro do_thrice> 
  <#nested 1> 
  <#nested 2> 
  <#nested 3> 
</#macro> 
<@do_thrice ; x> <#-- 用戶自定指令 使用";"代替"as" --> 
  ${x} Anything. 
</@do_thrice> 


<#macro repeat count> 
  <#list 1..count as x> 
    <#nested x, x/2, x==count> 
  </#list> 
</#macro> 
##1
<@repeat count=4 ; c, halfc, last> 
  ${c}. ${halfc}<#if last> Last!</#if> 
</@repeat> 
##2
<@repeat count=4 ; c, halfc> 
  ${c}. ${halfc} 
</@repeat> 
##3
<@repeat count=4> 
  Just repeat it... 
</@repeat> 
##marco test#####################################################################




show x =
<#assign x = 1>
${x} 
<#assign x = x + 3>
${x} 




## include namespace 命名空間 ##
<#import "lib.ftl" as my>  
<@my.copyright date="1999-2002"/> 
my.mail0 = ${my.mail}  
<#assign mail="jsmith@other.com" in my> 
my.mail1 = ${my.mail}  
<@my.copyright date="2002-2005"/> 
=====> ${my.libfunc("vvvvv")}




## 壓縮空白與多餘的換行 ## 
<#compress> 
<#assign users = [{"name":"Joe",        "hidden":false}, 
                  {"name":"James Bond", "hidden":true}, 
                  {"name":"Julia",      "hidden":false}]> 
List of users: 
<#list users as user> 
  <#if !user.hidden> 
  - ${user.name} 
  </#if> 
</#list> 
That's all. 
</#compress> 
## 壓縮成單行 ##
<@compress single_line=true> 
<#assign users = [{"name":"Joe",        "hidden":false}, 
                  {"name":"James Bond", "hidden":true}, 
                  {"name":"Julia",      "hidden":false}]> 
List of users: 
<#list users as user> 
  <#if !user.hidden> 
  - ${user.name} 
  </#if> 
</#list> 
That's all. 
</@compress> 





##定義方法##
${repeat2("What", 3)}
<#function repeat2 val num>
        <#local rtn = "">
        <#list 1..num as x>
                <#local rtn = rtn + val>
        </#list>
        <#return rtn>
</#function>
<#-- 若與macro命名重複會覆蓋原macro定義 -->





## 段落削減 ##
-- 
  (A忽略行首尾空白)<#t> 
  (B忽略行首尾空白)<#t> 
  (C忽略行首空白)<#lt> 
  (D)
  (E忽略行尾空白)<#rt> 
  (F) 
  (G關閉同行出現的消減指令)<#nt>    
  (H)
-- 



##字串包含特定字元
	<#if sql?lower_case?contains('select ')>
	<#elseif sql?lower_case?contains('insert ')>
	<#else>
	</#if>




## try catch ##
<#attempt> 
        show : ${"12345"?int}
<#recover> 
        show : error!!
</#attempt> 






## map ##
<#assign testMap = {"name":"Joe", "hidden":false, "num":1}>
<#list testMap?keys as x>
        key:${x} , value:${testMap[x]?string}
</#list>
<#list testMap?values as x>
        value:${x?string}
</#list>





###### 特別變數區 ######
## 變數名稱含特殊字元 ##
<#assign "this is a book" = "GOOD BOOK!">
${.vars["this is a book"]}

## current date ##
${.now?date}

## 模板名稱 ##
${.template_name}






## 數值format ##
${123456789} -> 會format
${123456789?c} -> 不會format
?c -> 只接受數值





## 布林值format ##
default : ${true?string} / ${false?string}
custom : ${true?string("是", "否")} / ${false?string("是", "否")}





## 對bean的操作 ##
${book.setTitle("xxxxxxx")}
show book.title -> ${book.title}
show book.title -> ${book.getTitle()}

????????????????????
<#assign testbean = 'gtu.freemarker.SimpleTest$TestModel'?new>
<#-- testbean ==> ${testbean?string} -->
TODO 尚待研究!!!
????????????????????





## test method (自訂method) ##
testMethod() ---> ${testMethod("aaaa", "bbbbb", "cccccc", "ddddd")}

<#include "footer.html"> 