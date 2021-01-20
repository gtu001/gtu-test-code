maven_bat_建立範例.md
----
#先設定自訂的JDK 然後切到要建立的project 再用指定的mvn 設定指定的properties 與自訂 profiles

# clean package 編譯結果為當前project底下
# clean install 會到m2/repository
# --settings path_to_settings.xml 使用指定的settings.xml


set JAVA_HOME=C:/Program Files/Java/jdk1.8.0_191

cd  D:\JavaEnv4\LIS\BAK\lis7-model\lis-model-policy-schema 

REM D:\my_tool\apache-maven-3.6.3-bin\bin\mvn  --settings D:\JavaEnv4\maven\settingsNexus.xml  clean package  -Drevision=7.1 -Dchangelist=-SNAPSHOT -P ENV-dev
D:\my_tool\apache-maven-3.6.3-bin\bin\mvn  --settings D:\JavaEnv4\maven\settingsNexus.xml  clean install  -Drevision=7.1 -Dchangelist=-SNAPSHOT -P ENV-dev

pause




