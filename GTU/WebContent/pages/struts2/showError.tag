        <!-- 訊息提示 -->
        <div class="ErrorMsg">
            <s:if test="hasActionErrors()">
                <table width="100%">
                    <s:iterator value="actionErrors">                       
                        <tr><td>
                            <s:property escape="false"/>
                        </td></tr>                                 
                    </s:iterator>
                </table>        
            </s:if>
            <s:if test="hasActionMessages()">
                <script type="text/javascript">
                    var msg = "";
                    <s:iterator value="actionMessages">
                        msg += "<s:property escape="false"/>\n";
                    </s:iterator>
                    if(msg != "") {
                        alert(msg);
                    }
                </script>
            </s:if>
        </div>
        
        
        addActionError("儲存錯誤:" + e.getMessage());  // action 加上