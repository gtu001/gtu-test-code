jenkins_install_安裝執行.md
---
	來源 https://www.jenkins.io/doc/book/installing/

  一共要run兩個container(1)

	$ docker image pull docker:dind

	$ docker network create jenkins

	$ docker container run --name jenkins-docker --rm --detach \
  --privileged --network jenkins --network-alias docker \
  --env DOCKER_TLS_CERTDIR=/certs \
  --volume jenkins-docker-certs:/certs/client \
  --volume jenkins-data:/var/jenkins_home \
  --publish 2376:2376 docker:dind

  一共要run兩個container(2)

	$ docker image pull jenkinsci/blueocean

	$ docker container run --name jenkins-blueocean (--rm) --detach \
  --network jenkins --env DOCKER_HOST=tcp://docker:2376 \
  --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 \
  --volume jenkins-data:/var/jenkins_home \
  --volume jenkins-docker-certs:/certs/client:ro \
  --publish 8080:8080 --publish 50000:50000 jenkinsci/blueocean 

  看第一次登入密碼
  	$ docker logs jenkins-blueocean

  	$ docker exec -it jenkins-blueocean bash 
  		cd /var/jenkins_home/secrets
  		cat initialAdminPassword




docker_jdk11鏡像安裝法 
---

	$ docker pull jenkins/jenkins:jdk11

	$ docker run --rm -ti -p 8080:8080 -p 50000:50000 -v jenkins-home:/var/jenkins_home jenkins/jenkins:jdk11







從Ｗar執行
---
	下載	http://mirrors.jenkins.io/war-stable/latest/jenkins.war

	$ java -jar jenkins.war

	輸入密碼從檔案 /home/gtu001/.jenkins/secrets/initialAdminPassword 


