kibana_install_安裝.md
---
	$ docker pull   docker.elastic.co/kibana/kibana:7.8.1

	$ docker run --link <elasticsearch_id>:elasticsearch -p 5601:5601   <kibana_image_id>
		Ex : docker run --link d0b2b340e457:elasticsearch -p 5601:5601  docker.elastic.co/kibana/kibana:7.8.1


	