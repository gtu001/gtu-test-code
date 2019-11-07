1.首先使用cmd打開命令列提示符， 右鍵以管理員身份運行。

2.輸入slmgr.vbs –upk卸載之前的系統啟用金鑰

3.假如你的系統為win10專業版
	則輸入slmgr /ipk W269N-WFGWX-YVC9B-4J6C9-T83GX
  假如你的系統為Win10企業版
  	則輸入slmgr /ipk NPPR9-FWDCX-D2C8J-H872K-2YT43命令

（假如你不知道自己的電腦系統， 可以右鍵我的電腦， 點擊系統進行查看）

4. 輸入slmgr /skms zh.us.to命令， 稍等片刻後， 則會提示“金鑰管理服務電腦名成功的設置為zh.us.to”字樣。

5.最後輸入命令“slmgr /ato”， 等待彈窗後， 恭喜你， 啟動成功！