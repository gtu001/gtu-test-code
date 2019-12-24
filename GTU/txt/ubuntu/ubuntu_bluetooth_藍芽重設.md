ubuntu 藍芽重設
---
	sudo apt-get install bluetooth bluez bluez-tools rfkill
		裝套件
	sudo rfkill list
		確認是否開啟 
	sudo rfkill unblock bluetooth
		重新開啟
	sudo service bluetooth start
		確認服務啟動


安裝 Bluetooth Manager
---
	sudo apt-get install blueman
		安裝藍芽套件
	搜尋 Bluetooth Manager
