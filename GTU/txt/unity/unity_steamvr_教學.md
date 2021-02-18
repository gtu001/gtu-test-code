unity_steamvr_教學.md
---
	安裝  https://assetstore.unity.com/packages/tools/integration/steamvr-plugin-32647

	上面選單
		選 Window > Package Manager > 左上角選 My Assets
			右邊輸入 Stream VR Plugin

		選 Window > Steam VR Input > Save and generate

		選 Window > Steam VR Input > Open binding UI <--開啟steamVR

	下面Project
		Assets > SteamVR > InteractionSystem > Samples 
		右邊 Interactions_Example.unity 點兩下

	錯誤:
		UnityEditor.BuildPlayerWindow+BuildMethodException: Error building Player: Currently selected scripting backend (IL2CPP) is not installed.
	到此找安裝
		https://unity3d.com/unity/whats-new/2019.4.19

  