>>>> 線上教學順序 <<<<

確認是否正常安裝
---
	minikube version
	
	
啟動 cluster
---
	minikube start
	
	

#檢視cluster狀態
正常會有兩個 server, client
---
	kubectl version
	

檢視 cluster 線上UI dashboard
---
	kubectl cluster-info
	
	
看建立的nodes
---
	kubectl get nodes
	
	
	
	
	
	
	
	
	
	
#使用 create 建立 deployment 管理一個 pod
pod 跑一個由你所提供的image 的 container
---
	kubectl create deployment hello-node --image=gcr.io/hello-minikube-zero-install/hello-node
	
	kubectl create deployment kubernetes-bootcamp --image=gcr.io/google-samples/kubernetes-bootcamp:v1
	

看這個 部屬的 instance
---
	kubectl get deployments
	====
		NAME         READY   UP-TO-DATE   AVAILABLE   AGE
		ello-node   1/1     1            1           1m
		

看 pod
---
	kubectl get pods
	====
		NAME                          READY     STATUS    RESTARTS   AGE
		hello-node-5f76cf6ccf-br9b5   1/1       Running   0          1m
		
		
看 cluster 的 events
---
	kubectl get events
	
	
看 kubectl 設定 
---
	kubectl config view
	
	
#預設 pod 使用的 ip 與 cluster共用
 hello-node 可用外部網路
---
	kubectl expose deployment hello-node --type=LoadBalancer --port=8080
 	Ps:
 		--type=LoadBalancer 表示要暴露 service 給外部 cluster
 		

看你建的 service
---
	kubectl get services
	====
		NAME         TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
		hello-node   LoadBalancer   10.108.144.78   <pending>     8080:30369/TCP   21s
		kubernetes   ClusterIP      10.96.0.1       <none>        443/TCP          23m
		
	Ps:
		雲端提供 load balance 外部IP將準備存取服務 LoadBalancer 讓 service 存取透過 minikube service 指令
		

啟動服務
---
	minikube service hello-node



看你建立的 pod 與 service
---
	kubectl get pod,svc -n kube-system


======================

增益功能
---
	minikube addons list
	
	
啟動關閉
---
	minikube addons enable <功能>
	minikube addons disable <功能>
	
======================

清除 resources
---
	kubectl delete service hello-node
	kubectl delete deployment hello-node

	
	
======================
# (用完請隨手關閉)
關機
---
	minikube stop
	
	
# (起不起來就得刪除重來)
刪除
---
	minikube delete