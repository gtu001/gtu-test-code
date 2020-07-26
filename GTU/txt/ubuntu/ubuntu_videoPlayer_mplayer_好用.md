ubuntu_videoPlayer_mplayer_好用.md
---
	sudo apt-get install mplayer 


旋轉90度
	mplayer -vf rotate=1  ".../video.mp4" 

        mirror ---> -vf-add mirror 
        90 ---> -vf-add rotate=1 
        180 ---> -vf-add flip 
        270 ---> -vf-add mirror,rotate=1 
        180_mirror ---> -vf-add mirror,flip 
        


	按"o"開啟進度
	按"左右" 快進退15秒
	按"上下" 快進退1分鐘
	按"f" 全螢幕
	按"T" 最上層


快速鍵總表
	<- and ->	Seek backward/forward 10 seconds.
	up and down	Seek forward/backward 1 minute.
	pgup and pgdown	Seek forward/backward 10 minutes.
	[ and ]	Decrease/increase current playback speed by 10%.
	{ and }	Halve/double current playback speed.
	backspace	Reset playback speed to normal.
	< and >	Go backward/forward in the playlist.
	ENTER	Go forward in the playlist, even over the end.
	HOME and END	next/previous playtree entry in the parent list
	INS and DEL (ASX playlist only)	next/previous alternative source.
	p / SPACE	Pause (pressing again unpauses).
	.	Step forward. Pressing once will pause movie, every consecutive press will play one frame and then go into pause mode again (any other key unpauses).
	q / ESC	Stop playing and quit.
	+ and -	Adjust audio delay by +/- 0.1 seconds.
	/ and *	Decrease/increase volume.
	9 and 0	Decrease/increase volume.
	( and )	Adjust audio balance in favor of left/right channel.
	m	Mute sound.
	_ (MPEG-TS and libavformat only)	Cycle through the available video tracks.
	# (DVD, MPEG, Matroska, AVI and libavformat only)	Cycle through the available audio tracks.
	TAB (MPEG-TS only)	Cycle through the available programs.
	f	Toggle fullscreen (also see -fs).
	T	Toggle stay-on-top (also see -ontop).
	w and e	Decrease/increase pan-and-scan range.
	o	Toggle OSD states: none / seek / seek + timer / seek + timer + total time.
	d	Toggle frame dropping states: none / skip display / skip decoding (see -framedrop and -hardframedrop).
	v	Toggle subtitle visibility.
	j	Cycle through the available subtitles.
	y and g	Step forward/backward in the subtitle list.
	F	Toggle displaying forced subtitles .
	a	Toggle subtitle alignment: top / middle / bottom.
	x and z	Adjust subtitle delay by +/- 0.1 seconds.
	r and t	Move subtitles up/down.
	i (-edlout mode only)	Set start or end of an EDL skip and write it out to the given file.
	s (-vf screenshot only)	Take a screenshot.
	S (-vf screenshot only)	Start/stop taking screenshots.
	I	Show filename on the OSD.
	! and @	Seek to the beginning of the previous/next chapter.
	D (-vo xvmc, -vf yadif, -vf kerndeint only)	Activate/deactivate deinterlacer.