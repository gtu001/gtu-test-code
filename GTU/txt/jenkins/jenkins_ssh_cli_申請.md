jenkins_ssh_cli_申請.md
======

來源 https://www.jenkins.io/doc/book/managing/cli/

(0)
	Manage Jenkins -> Configure Global Security
	最底下
	SSH Server -> SSHD Port -> Fixed: 53801 (官方範例)


(1)
	設定網頁
	People->Admin->Configure
		https://localhost:8080/user/<USERNAME>/configure
	於 SSH Public Keys 區塊
	貼上以下 SSH Public Key



(2)
	bash-4.4$$$$$ cd /var/jenkins_home

	bash-4.4$$$$$ whoami 
	jenkins <--似乎要用此帳號

	bash-4.4$$$$$ ssh-keygen

	Generating public/private rsa key pair.
	Enter file in which to save the key (/var/jenkins_home/.ssh/id_rsa): 
	Created directory '/var/jenkins_home/.ssh'.
	Enter passphrase (empty for no passphrase): (免填)
	Enter same passphrase again: (免填)
	Your identification has been saved in /var/jenkins_home/.ssh/id_rsa.
	Your public key has been saved in /var/jenkins_home/.ssh/id_rsa.pub.
	The key fingerprint is:
	SHA256:8J2V27vWdVBIgg/EkA6tlsw2LfMYjqq19AQtnpAUcYg jenkins@40c10c9600a4
	The key's randomart image is:
	+---[RSA 2048]----+
	|.oo.   ..=...... |
	|E.o   . o +  o. .|
	| .   o.*   oo  . |
	|.. .  %oo. o.o.  |
	|o o .= BS o . .. |
	| o +. o .      .o|
	|  =..         ..o|
	| o.+          ...|
	|... .        ..  |
	+----[SHA256]-----+

	bash-4.4$$$$$ ls -l .ssh

	total 8
	-rw------- 1 jenkins jenkins 1831 Aug  3 12:00 id_rsa
	-rw-r--r-- 1 jenkins jenkins  402 Aug  3 12:00 id_rsa.pub

	bash-4.4$$$$$ cat .ssh/id_rsa.pub

	SSH PUBLIC KEY
	=================================================================
	ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDUHnuPB2ov/12JAAx/46s3xNOYX8/BOHxGzRnPXMC4LlwfjxPP7kq83GY3SGSQAOd1xxIcd8LxSDXaPko25D6oI3FS+cZ7RKN2rWQU6pyqyQxmOWVox7xuAmqP1mVHGWsNLo4xv6hw/wSpY1rsnr41ZYn3Be5DbOQdTMle75uo3r9O6vAI7PMYIwFdKu6Tl5HDOe93OBih5INqHcYDW9BCRJxsJxzhUi98W30TRx+46rZt8qL22nw3XaHf2xIvx+sgF/3vqtrZAPf+GTQcP2WGPY+J24Lb15CBBQ2mtf/pWpAMxasjIcbbjsjndxeeZ+xUqhSLtTCdQH/RYysPeqbh jenkins@40c10c9600a4
	=================================================================

	bash-4.4$$$$$


(3)
	$ ssh -l <account> -p <port> <ip> help
		Ex : ssh -l admin -p 53801 localhost help



-------------------------------------
方法2 使用 cli.jar
	下載 http://<jenkins_url>/jnlpJars/jenkins-cli.jar

	Ex : java -jar jenkins-cli.jar -s http://jenkins.example.com:8080/ -i /root/.ssh/id_rsa safe-restart
