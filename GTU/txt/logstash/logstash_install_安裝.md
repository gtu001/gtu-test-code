logstash_install_安裝.md
---
	$ docker pull docker.elastic.co/logstash/logstash:7.8.1

	$ docker run --rm -it -v ~/pipeline/:C/Users/wistronits/logstash/ docker.elastic.co/logstash/logstash:7.8.1

	 docker run --rm -it --mount 'type=volume,src=~/pipeline/,dst=C/Users/wistronits/logstash/'  docker.elastic.co/logstash/logstash:7.8.1



	 docker run --rm -it   --mount type=volume,src=AAA,dst=C:/Users/wistronits/logstash/    easybox/test:latest 



### 還沒看 
	https://www.elastic.co/guide/en/logstash/7.8/docker-config.html