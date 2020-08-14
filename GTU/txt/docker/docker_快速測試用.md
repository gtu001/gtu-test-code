docker_快速測試用.md
---
	docker pull busybox 

	docker run -it -d --mount type=volume,src=test_vol,dst=/var busybox


	docker run -d --name devtest   --mount source=test_vol,target=/app    nginx:latest

	docker run -d --name devtest   -v /C/Users/wistronits/MyDockerVolume001:/app   nginx:latest


	