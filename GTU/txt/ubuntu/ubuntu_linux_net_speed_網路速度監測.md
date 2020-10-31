ubuntu_linux_net_speed_網路速度監測.md


---
nethogs 簡單好用
---


總結了ubuntu下常用的網路檢視/監控工具，包括nethogs，ethstatus，bmon，Netspeed。

1. nethogs

nethogs可以檢視實時程序網路佔用。 
安裝： sudo apt install nethogs 
檢視網路狀態： nethogs eth0 
即 nethogs + 網絡卡名稱，雙擊table會出現備選網絡卡名稱

Detail：https://www.cnblogs.com/aaron-agu/p/5743141.html


2. ethstatus

ethstatus可以監控實時的網絡卡頻寬佔用。 
這個軟體能顯示當前網絡卡的 RX 和 TX 速率，單位是Byte 
安裝 sudo apt-get install ethstatus 
檢視 ADSL 的速度 sudo ethstatus -i eth0 
檢視 網絡卡 的速度 sudo ethstatus -i eth0 
eth0是網絡卡名稱，可以換為自己機器的網絡卡名稱。 
檢視網絡卡名稱 ifconfig

Refer: http://blog.csdn.net/mmsxst10062710/article/details/41313067


3. bmon

安裝 sudo apt-get install bmon 
檢視網路 bmon -p eth0 
- 輸入g控制流量面板的顯示和隱藏 
- 輸入d控制詳情資訊的顯示和隱藏 
- 輸入q退出面板 
網頁輸出命令 bmon -I distribution:multicast -o null -O html:path=/var/htdocs/ 
可以配合nginx部署通過瀏覽器監控網路

http://blog.csdn.net/jpiverson/article/details/11612843


4. Netspeed

Netspeed是擁有GUI介面實時顯示網速的工具。 
新增源 sudo add-apt-repository ppa:ferramroberto/linuxfreedomlucid && sudo apt-get update 
安裝 sudo apt-get install netspeed 
