#!/bin/bash
MP4FILE=$(ls "$1" |grep .mp4)
for filename in $MP4FILE
do 
	name=`echo "$filename" | sed -e "s/.mp4$//g"`
	ffmpeg -i "$1"/$filename -b:a 192K -vn "$1"/$name.mp3
done