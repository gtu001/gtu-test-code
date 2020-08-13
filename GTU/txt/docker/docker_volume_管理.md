docker_volume_管理.md
---
	列出所有volume
		$ docker volume ls

	建立volume
		$ docker volume create my-vol

	移除volume
		$ docker volume rm my-vol

	看詳細
		$ docker volume inspect my-vol


	PS : container 刪除後才能刪除 volume



	Ex : $ docker run -d  --name devtest  --mount src=my-vol,dst=/app,readonly    nginx:latest
		 $ docker run -d  --name devtest  -v my-vol:/app:ro  nginx:latest



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