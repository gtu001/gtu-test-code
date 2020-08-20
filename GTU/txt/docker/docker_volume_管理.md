docker_volume_管理.md
---
	**重要 (win7 系統 docker )**
		必須位置為user底下 
			如 -v /C/Users/wistronits/:/app
				Ex : docker run -d  --name devtest   -v /C/Users/wistronits/MyDockerVolume001:/app   nginx:latest
		然後還要用 
			"C:\Program Files\Docker Toolbox\kitematic\Kitematic.exe"
			去修改 volume 的 share folder



	列出所有volume
		$ docker volume ls

	建立volume
		$ docker volume create my-vol


	建立volume 自訂位置
		win 非常難用(新版docker desktop可用)
			以下兩者同
			$ docker volume create --driver local --opt type=none --opt device=/d/docker_volume_001 --opt o=bind test_vol
			  docker volume create --driver local --opt type=none --opt device=d:\docker_volume_001 --opt o=bind test_vol
			  docker volume create --driver local --opt type=none --opt device=//C/Users/wistronits/MyDockerVolume001 --opt o=bind test_vol
			  docker volume create --driver local --opt type=nfs --opt o=addr=192.168.1.1,rw --opt device=/d/docker_volume_001  test_vol

		ubuntu 測試可用
			  docker volume create --driver local --opt type=none --opt device=/media/gtu001/OLD_D --opt o=bind test_vol
			  docker run -d --name devtest   --mount source=test_vol,target=/app    nginx:latest
		  


		** 對應run指令 **
			$ docker run -it --rm \
			    --mount type=volume,dst=/container/path,volume-driver=local,volume-opt=type=none,volume-opt=o=bind,volume-opt=device=/home/user/test \
			    foo
		** 對應docker-compose **
			  volumes:
			    bind-test:
			      driver: local
			      driver_opts:
			        type: none
			        o: bind
			        device: /home/user/test


	移除volume
		$ docker volume rm test_vol

	看詳細
		$ docker volume inspect test_vol


	PS : container 刪除後才能刪除 volume



	Ex : $ docker run -d  --name devtest  --mount src=test_vol,dst=/app,readonly    nginx:latest
		 $ docker run -d  --name devtest  -v test_vol:/app:ro  nginx:latest



-----------
SSH 遠端 volume
---
	先裝 plugin
		$ docker plugin install --grant-all-permissions vieux/sshfs

	建立遠端 volume
		$ docker volume create --driver vieux/sshfs \
			  -o sshcmd=test@node2:/home/test \
			  -o password=testpassword \
			  sshvolume

	建立 container 帶有遠端 volume
		$ docker run -d \
			  --name sshfs-container \
			  --volume-driver vieux/sshfs \
			  --mount src=sshvolume,target=/app,volume-opt=sshcmd=test@node2:/home/test,volume-opt=password=testpassword \
			  nginx:latest



###
後面還沒看
	https://docs.docker.com/storage/volumes/