SonarQube_教學.md
---
	官方下載SonarQube 社群版 https://www.sonarqube.org/success-download-community-edition/

	解壓

	修改 SonarQube/conf/wrapper.conf
	指令java11 JDK路徑(必須要用jdk11)  ---> https://www.oracle.com/java/technologies/javase-downloads.html
	==========
		wrapper.java.command=D:/sonarqube-developer-8.7.0.41497/jdk-11.0.10/bin/java.exe

	啟動後開瀏覽器 http://localhost:9000/
	帳密 admin/admin

	輸入projectKey會產生token
	projectKey = 12345abcde
	token      = 26e67eedd421e3a98186c81d3e9dc9f1d906d903
	

	使用Command
	mvn  clean  compile  sonar:sonar   
	 (-Dsonar.analysis.mode=preview -Dsonar.issuesReport.html.enable=true  (deprecated))
	 -Dsonar.projectKey=12345abcde -Dsonar.host.url=http://localhost:9000 -Dsonar.login=26e67eedd421e3a98186c81d3e9dc9f1d906d903   

	說明 
		產表  -Dsonar.analysis.mode=preview -Dsonar.issuesReport.html.enable=true (deprecated)



	用以下bat去掃
	最後可用的bat 如下
	==========
	set JAVA_HOME=C:/Program Files/Java/jdk1.8.0_191

	cd D:\JavaEnv4\LIS\BAK\lis7-model

	D:\

	D:\app\apache-maven-3.6.3\bin/mvn   --settings D:\JavaEnv4\maven\settingsNexus.xml   clean  compile  sonar:sonar   -Drevision=7.1 -Dchangelist=-SNAPSHOT -Dsonar.projectKey=12345abcde    -Dsonar.host.url=http://localhost:9000    -Dsonar.login=26e67eedd421e3a98186c81d3e9dc9f1d906d903  -P ENV-dev  

	pause
	==========




	Sonar預設資料庫
	==========
		DB URL : jdbc:h2:tcp://localhost:9092/sonar
		帳密 : 不用填
		driver : org.h2.Driver
		---
			顯示所有資料庫 SQL : SHOW TABLES