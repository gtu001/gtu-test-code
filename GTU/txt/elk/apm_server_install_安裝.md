apm_server_install_安裝.md
---
	Linux 
	---
		curl -L -O https://artifacts.elastic.co/downloads/apm-server/apm-server-7.9.0-linux-x86_64.tar.gz
		tar xzvf apm-server-7.9.0-linux-x86_64.tar.gz


	Win
	---
		https://www.elastic.co/downloads/apm



	docker
	---
		$ docker pull docker.elastic.co/apm/apm-server:7.9.0

		下載設定檔
			curl -L -O https://raw.githubusercontent.com/elastic/apm-server/7.9/apm-server.docker.yml


		建立
			linux : 
			docker run -d   --name=apm-server   --user=apm-server   --volume="$(pwd)/apm-server.docker.yml:/usr/share/apm-server/apm-server.yml:ro"   docker.elastic.co/apm/apm-server:7.9.0   --strict.perms=false -e   -E output.elasticsearch.hosts=["localhost:9200"]

			win :
			docker run -d   --name=apm-server   --user=apm-server   --volume="%CD%/apm-server.docker.yml:/usr/share/apm-server/apm-server.yml:ro"   docker.elastic.co/apm/apm-server:7.9.0   --strict.perms=false -e   -E output.elasticsearch.hosts=["localhost:9200"]


			FROM docker.elastic.co/apm/apm-server:7.9.0
			COPY apm-server.yml /usr/share/apm-server/apm-server.yml
			USER root
			RUN chown root:apm-server /usr/share/apm-server/apm-server.yml
			USER apm-server