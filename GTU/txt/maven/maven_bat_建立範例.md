maven_bat_建立範例.md
----
#先設定自訂的JDK 然後切到要建立的project 再用指定的mvn 設定指定的properties 與自訂 profiles


set JAVA_HOME=C:/Program Files/Java/jdk1.8.0_191

cd  D:\JavaEnv4\LIS\BAK\lis7-model\lis-model-policy-schema 

D:\my_tool\apache-maven-3.6.3-bin\bin\mvn clean package  -Drevision=7.1 -Dchangelist=-SNAPSHOT -P ENV-dev

pause