基本設定
===
(1)
	ESP文件安裝方式
	1.在下邊壓縮包里任選一個ESP文件丟入遊戲DATA文件夾里。
	C:\Users\gtu001\AppData\Local\Fallout4VR\plugins.txt
	在文本末尾添換行加你放入DATA里的ESP文件全名。


(2)
---
	開啟 D:\SteamLibrary\steamapps\common\Fallout 4 VR\Fallout4Prefs.ini
	或開啟 C:\Users\gtu001\Documents\my games\Fallout4VR\Fallout4Prefs.ini

	在 [Launcher] 下面加入
		bEnableFileSelection=1

(3)
---
	開啟 D:\SteamLibrary\steamapps\common\Fallout 4 VR\Fallout4.ini
	或開啟 C:\Users\gtu001\Documents\my games\Fallout4VR\Fallout4.ini

	換掉原來 sResourceDataDirsFinal=STRINGS\
	改成以下
	sResourceDataDirsFinal=STRINGS\, TEXTURES\, MUSIC\, SOUND\, INTERFACE\, MESHES\, PROGRAMS\, MATERIALS\, LODSETTINGS\, VIS\, MISC\, SCRIPTS\, SHADERSFX\

(4)
---
	開啟 C:\Users\gtu001\Documents\my games\Fallout4VR\Fallout4Custom.ini: 
	加入
		[Archive]
		bInvalidateOlderFiles=1
		sResourceDataDirsFinal=