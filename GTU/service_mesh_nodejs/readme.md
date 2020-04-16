
set PROJECT_ID=1

docker build -t gcr.io/%PROJECT_ID%/service-mesh:v1 .

docker run -p 8001:8001 gcr.io/%PROJECT_ID%/service-mesh:v1



docker build -t gcr.io/1/service-mesh:v1 .

docker run -p 8001:8001 gcr.io/1/service-mesh:v1

docker rmi gcr.io/1/service-mesh:v1


//////https://medium.com/faun/building-your-very-own-service-mesh-4723895d061d
