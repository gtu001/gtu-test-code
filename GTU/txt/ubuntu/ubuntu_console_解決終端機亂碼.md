ubuntu_解決終端機亂碼
---
	

## 用vim配置语言环境变量

$ vi /etc/environment

## 改成：

```
LANG=”en_US.UTF-8"
LANGUAGE=”en_US:en”
```

$ sudo vi /var/lib/locales/supported.d/local

## 改成

```
en_US.UTF-8 UTF-8
```

## 保存后，执行命令：

$ sudo locale-gen

$ sudo vi /etc/default/locale

## 修改为：

```
LANG=”en_US.UTF-8"
LANGUAGE=”en_US:en”
```

## 重启Ubuntu Server

$ sudo reboot

————————————————
版权声明：本文为CSDN博主「zxp-smail」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u012732259/java/article/details/70209436