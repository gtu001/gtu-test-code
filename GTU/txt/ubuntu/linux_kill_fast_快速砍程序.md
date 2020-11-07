linux_kill_fast_快速砍程序.md

	$ kill -9 $(ps -aux | grep vlc | awk '{print $2}')