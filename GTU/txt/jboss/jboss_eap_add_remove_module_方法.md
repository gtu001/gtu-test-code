
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

加入模組
---	
	$ module add --name=<package>  --resources=<path_to_jar>
	$ module add --name=org.apache.openjpa  --resources=D:\work_tool\20180223\jboss-eap-6.1.0\jboss-eap-7.2\modules\system\layers\base\org\apache\openjpa\main\jipijapa-openjpa-7.2.0.GA-redhat-00005.jar

移除模組
---
	$ module remove --name=<package> 
	$ module remove --name=org.apache.openjpa  

加入系統參數
---
	$ /system-property=<Key>:add(value=<Value>)
	$ /system-property=org.apache.catalina.session.ManagerBase.SESSION_ID_LENGTH:add(value=19)



