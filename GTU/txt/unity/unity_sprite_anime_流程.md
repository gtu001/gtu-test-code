unity_sprite_anime_流程.md
---
建立Anime,Contoller流程
---
	1.選多個連續圖 往事拖曳

	2.會產生一個anime, 取一個名子放到/Animation/Animation

	3.也會產生一個 controller, 放到/Animation/Controller
		Ps : 若同個角色不須多個controller 
				處理方式 :   1.連點兩下controller
							2.滑鼠右鍵 Create State -> Empty
							3.改名 , 然後拖曳 Anime 至 Motion


建立Sprite流程
---
	1.GameObject -> 2D Object -> Sprite
	2.右邊 Add Component -> Animator
	3.拖曳 Controller 至 Controller