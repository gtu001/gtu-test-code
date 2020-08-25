顯示硬碟狀態
---
	df -h


顯示硬碟目前mount狀態
---
	cat /etc/mtab 
	cat /proc/mounts


顯示錯誤報告
---
	dmesg |  grep  sdb1  -B 5 -A 5 -n
	dmesg |  sed '794,801!d' > xxxxxx.txt


修理硬碟
---
	sudo fsck -y /dev/sdb1


查詢目前掛載清單
---
	mount -v | grep "^/" | awk '{print "\nPartition identifier: " $1  "\n Mountpoint: "  $3}'

重新掛載指令
---
	sudo mount -o remount,bind,rw  <partition_identifier>  <mount_point>
	Ex : 
		sudo mount -o  remount,bind,rw  /dev/sdb1  /media/gtu001/OLD_D



最後證實有效
---
	sudo umount -v /dev/sdb1 

	sudo ntfsfix /dev/sdb1
	sudo mkdir /media/gtu001/OLD_D
	sudo mount -o rw /dev/sdb1  /media/gtu001/OLD_D
