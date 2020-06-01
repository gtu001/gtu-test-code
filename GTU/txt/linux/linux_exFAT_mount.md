linux_exFAT_mount.md
---
掛載

	sudo apt-get install exfat-fuse exfat-utils

	sudo mkdir /media/exfat

	sudo mount -t exfat /dev/sdc1 /media/exfat

---
移除

	sudo umount /dev/sdc1