winlogbeat_install_安裝.md
---
	下載解壓縮至 C:\Program Files\Winlogbeat
		https://www.elastic.co/downloads/beats/winlogbeat



	用管理者模式開啟 powershell 
	$  cd 'C:\Program Files\Winlogbeat'
	$  .\install-service-winlogbeat.ps1



	修改 winlogbeat.yml 

		加入 連接elasticsearch, kibana
		output.elasticsearch:
		    hosts: ["elasticsearch_url:9200"]
		    username : ""
		    password : ""

		setup.kibana:
		    host : "kibana_url:5601"
		    username : ""
		    password : ""


		屬性設定
			指定log事件到螢幕
			以下為預設
			winlogbeat.event_logs:
			  - name: Application
			  - name: Security
			  - name: System

			取得有效的event logs 
				執行 powershell -> Get-EventLog *

			設定log位置
				logging.level: info
				logging.to_files: true
				logging.files:
				  path: C:\ProgramData\winlogbeat\Logs
				  name: winlogbeat
				  keepfiles: 7
				  permissions: 0644
				


	測試設定檔是否正確 **重要**
		open powershell
		$ cd 'C:\Program Files\Winlogbeat'
		$  .\winlogbeat.exe test config -c .\winlogbeat.yml -e


	設定預設assets
		open powershell
		$ cd 'C:\Program Files\Winlogbeat'
		$  .\winlogbeat.exe setup -e


	啟動
		open powershell
		$ cd 'C:\Program Files\Winlogbeat'
		$  Start-Service winlogbeat

		無法啟動可能是 yml 錯誤


	啟動錯誤排除 **重要**
		open powershell
		$ cd 'C:\Program Files\Winlogbeat'
		$ .\winlogbeat.exe -c winlogbeat.yml -e -v -d "*"
			-e 前景作業
			-c winlogbeat.yml 指定使用設定檔
			-d "*" 指定verbose內容
			-v Log Level INFO

	簡易執行
		open powershell
		$ cd 'C:\Program Files\Winlogbeat'
		$ .\winlogbeat.exe -e -v 



安裝腳本
	curl -XPUT http://elasticsearch:9200/_template/winlogbeat -d@/path/to/winlogbeat.template.json


重設 winlogbeat
    1)curl -XDELETE http://elasticsearch:9200/winlogbeat-*
    2)Stop Winlogbeat.
    3)Delete the .winlogbeat.yml registry file.
    4)Start Winlogbeat
