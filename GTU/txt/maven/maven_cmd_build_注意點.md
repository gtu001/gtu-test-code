maven_cmd_build_注意點.md
---
	1.確認需要替換此檔
		apache-maven-3.6.3-bin\conf\settings.xml
			須注意實際產生位置 <localRepository></localRepository>

	2.切到該專案底下

	3.$ mvn clean install -P profiles_key (設定Profiles)

	Ex : D:\JavaEnv4\LIS\BAK\lis7-model>D:\my_tool\apache-maven-3.6.3-bin\bin\mvn clean install -P ENV-dev