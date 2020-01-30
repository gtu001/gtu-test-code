
1.先把server on起來
----

2.在server所在目錄底下
---
  Ex : D:\work_tool\20180223\jboss-eap-6.1.0\jboss-eap-6.4\bin
  輸入 
    $ EAP_HOME/bin/jboss-cli.bat 
  會顯示[disconnect]
  再輸入
    $ connect


=======

加入系統參數
---
	opt1
		$ /system-property=<Key>:add(value=<Value>)
		$ /system-property=org.apache.catalina.session.ManagerBase.SESSION_ID_LENGTH:add(value=19)


	opt2
		編輯此檔 ‪D:\work_tool\20180223\jboss-eap-6.1.0\jboss-eap-7.2\standalone\configuration\standalone.xml
		在</extensions>結尾後加入
		<system-properties>
	        <property name="org.apache.catalina.session.ManagerBase.SESSION_ID_LENGTH" value="19"/>
	    </system-properties>