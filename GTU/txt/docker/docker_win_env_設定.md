docker_win_env_設定.md
---

	echo %DOCKER_CERT_PATH%
	舊的位置
		C:\Users\wistronits\.docker\machine\machines\default

	新的位置
		C:\Users\wistronits\.docker\machine\certs
	需要改掉


	
	echo %DOCKER_HOST%
	舊的為
		tcp://192.168.99.100:2376
	新的為
		tcp://localhost:2375
	需要改掉


	移除
		DOCKER_TLS_VERIFY


	Settings -> General -> Expose daemon on tcp://localhost:2375 without TLS 打勾