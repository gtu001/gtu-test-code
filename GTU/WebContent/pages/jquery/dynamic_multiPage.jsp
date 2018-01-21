<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.ebao.ls.arap.cash.ctrl.bankauth.keyin.AddBankSealRecordForm"%>
<%
                AddBankSealRecordForm form = (AddBankSealRecordForm)request.getAttribute("actionForm");
                
                String pageNo = (String)request.getAttribute("pageNo");//當前頁碼
                String pageJsonContent = (String)request.getAttribute("pageJsonContent");//之前設定的資料(再度返回之前編輯的頁面)
                String fullPageJsonContent = org.apache.commons.lang.StringUtils.defaultString(form.getPageJsonContent());//全部頁面資料
%>
 
 
 
                                                                               
                                                                               
<script language="text/javascript">
//序列化頁面資訊
$.fn.serializeObjet = function(){
                var o = {};
                var a = this.serializeArray();
                $.each(a,function(){
                                if (o[this.name]){
                                                if(!o[this.name].push){
                                                                o[this.name] = [o[this.name]];
                                                }
                                                o[this.name].push(this.value || '');
                                }else{
                                                o[this.name] = this.value || '';
                                }
                });
                return o;
}
 
//取得換頁初始化資訊
function initCheckFromExistPageContent(){
                var data = JSON.parse('<%=pageJsonContent%>');
                //alert('<%=pageJsonContent%>');
                if(data != null){
                                for(var id in data){
                                                if(id == 'pageJsonContent'){
                                                                continue;
                                                }
                                                var valArry = data[id];
                                                if(typeof(valArry) == 'object'){
                                                                if(valArry.length == 1){
                                                                                $("[name='"+id+"']").val(valArry[0]);
                                                                }else{
                                                                                var jqObj = $("[name='"+id+"']");
                                                                                for(var ii = 0 ; ii < jqObj.length ; ii ++){
                                                                                                $(jqObj).eq(ii).val(valArry[ii]);
                                                                                }
                                                                }
                                                }else if(typeof(valArry) == 'string'){
                                                                $("[name='"+id+"']").val(valArry);
                                                }
                                }
                                return true;//帶出前次資料
                }else{
                                return false;//無前次資料
                }
}
 
 
//換頁前取得畫面資料 changePageForm為送出分頁的新form若無可用form1
function submitBeforeChangePage(changePageForm){
                var dot = $(document.form1).serializeObjet();
                $.extend(dot, {"orign_pageNo":"<%=pageNo%>"});
                //delete dot['pageJsonContent'];
                $(changePageForm).find("[name='tempPageJsonContent']").remove();
                $(changePageForm).append("<textarea name='tempPageJsonContent'>"+JSON.stringify(dot)+"</textarea>");//tempPageJsonContent只傳當前分頁資訊
}
</script>
 
<!-- 儲存每個分頁資訊 -->
<textarea name="pageJsonContent" cols="100" rows="25" style="display: none"><%=fullPageJsonContent%></textarea>
 
 
 
//JAVA---------------------------------------
    /**
     * changePage 換頁要暫存畫面資料到session
    */
    private void changePageProcess(HttpServletRequest request, AddBankSealRecordForm sealForm){
        String tempPageJsonContent = request.getParameter("tempPageJsonContent");
        try {
            JSONObject parameterMap = new JSONObject();
            if(isNotBlank(tempPageJsonContent)){
                parameterMap = new JSONObject(tempPageJsonContent);
            }
           
            JSONObject formMap = new JSONObject();
            if(parameterMap.has("pageJsonContent") && isNotBlank(parameterMap.get("pageJsonContent").toString())){
                formMap = new JSONObject(parameterMap.get("pageJsonContent").toString());
                parameterMap.remove("pageJsonContent");
            }
            
            //紀錄當前頁面資訊
            if(parameterMap.has("orign_pageNo")){
                int pageNo = Integer.parseInt("" + parameterMap.getString("orign_pageNo"));
                formMap.put(String.valueOf(pageNo), parameterMap);
                sealForm.setPageJsonContent(formMap.toString());
                Log.info(this.getClass(), "#儲存pageJsonContent :" + pageNo);
            }
           
            //取得新頁面編號
            if(isNotBlank(request.getParameter("pageNo"))){
                int pageNo = Integer.parseInt(request.getParameter("pageNo"));
                if(formMap.has(String.valueOf(pageNo))){
                    parameterMap = (JSONObject)formMap.get(String.valueOf(pageNo));
                    //將之前紀錄的資訊送到頁面
                    request.setAttribute("pageJsonContent", parameterMap.toString());
                    Log.info(this.getClass(), "#讀取pageJsonContent :" + pageNo);
                }else{
                    Log.info(this.getClass(), "#無資料可讀取pageJsonContent :" + pageNo);
                }
            }
        } catch (Exception ex) {
            Log.error(this.getClass(), ex.getMessage(), ex);
        }
    }