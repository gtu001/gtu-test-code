package main

import (
	"fmt"
	"time"
	//	"reflect"
	"strings"
	"regexp"
	//	"strconv"
	//	"gtu/string/stringUtil"
)

func main() {
	startTime := time.Now()

	fmt.Println("startTime : ", startTime)

	fmt.Println("Year : ", startTime.Year())

	fmt.Println("formatDate---", formatDate("yyyy/MM/dd HH:mm:ss.SSS", startTime, false))

	fmt.Println("currentTimeMillis---" , currentTimeMillis())

	fmt.Println("done...")
}

func currentTimeMillis() int64 {
	return time.Now().UnixNano()/1e6
}

func formatDate(format string, date time.Time, isChineseYear bool) string {
	year := ""
	if isChineseYear {
		year = fmt.Sprintf("%03v", date.Year()-1911)
	} else {
		year = fmt.Sprintf("%04v", date.Year())
	}
	month := fmt.Sprintf("%02v", int(date.Month()))
	day := fmt.Sprintf("%02v", date.Day())
	hour := fmt.Sprintf("%02v", date.Hour())
	minute := fmt.Sprintf("%02v", date.Minute())
	second := fmt.Sprintf("%02v", date.Second())
	millsec := fmt.Sprintf("%03v", date.Nanosecond())[:4]
	format = strings.Replace(format, "yyyy", year, -1)
	format = strings.Replace(format, "MM", month, -1)
	format = strings.Replace(format, "dd", day, -1)
	format = strings.Replace(format, "HH", hour, -1)
	format = strings.Replace(format, "mm", minute, -1)
	format = strings.Replace(format, "ss", second, -1)
	re,_ := regexp.Compile("S+")
	tmpFormat := re.ReplaceAll([]byte(format), []byte(millsec))
	format = string(tmpFormat)
	return format
}
