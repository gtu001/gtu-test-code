package main

import (
	"fmt"
	"strings"
    "regexp"
    "reflect"
    //"strconv"
)

func main() {
    fmt.Println("TrimAllSpace-->" , TrimAllSpace("  dd  dd  "))
    fmt.Println("Trim-->" , Trim("  dd  dd  "))
    fmt.Println("GetChinese-->" , GetChinese("  dfasdfl 測試 lksjdf 中文 sldfjsdlf   "))
    fmt.Println("HasChinese-->" , HasChinese("  dfasdfl 測試 lksjdf 中文 sldfjsdlf   "))
    fmt.Println("HasChinese-->" , HasChinese("  dfasdfl  lksjdf  sldfjsdlf   "))
    fmt.Println("Left Padding-->" , Padding("aaa", 10, 'x', true))
    fmt.Println("Right Padding-->" , Padding("aaa", 10, 'x', false))
    fmt.Println("IsNumber-->" , IsNumber(-12354.566))
   // tester()
}

func Trim(strVal string) string {
	return strings.Trim(strVal, " ")
}

func TrimAllSpace(strVal string) string {
	var resultStr string = ""
    for _, r := range strVal {
        c := string(r)
        if c != " " {
        	resultStr += c
        }
    }
	return resultStr
}

func TypeOf(value interface{}) string {
	return fmt.Sprintf("%v", reflect.TypeOf(value))
}

func ValueOf(value interface{}) string {
	return fmt.Sprintf("value:[%v], type:[%T]", value, value)
}

func GetChinese(strVal string) string {
	ptn,_ := regexp.Compile("[\u4e00-\u9fa5]")
	intArry := ptn.FindAllIndex ([]byte(strVal), -1)
	if len(intArry) == 0 {
		return ""
	}
	rtnString := ""
	for _,subArry := range intArry {
		rtnString += strVal[subArry[0] : subArry[1]]
	}
	return rtnString
}

func HasChinese(strVal string) bool {
	return len(GetChinese(strVal)) > 0
}

func Padding(strVal string, length int, c rune, isLeft bool) string {
	appendLen := length - len(strVal)
	if appendLen <= 0 {
		return strVal
	}
	for i := 0; i < appendLen; i ++ {
		if isLeft {
			strVal = string(c) + strVal
		}else {
			strVal += string(c)
		}
	}
	return strVal
}

func IsNumber(dataVal interface{}) bool {
	if dataVal == nil {
		return false
	}
	strVal := fmt.Sprintf("%v", dataVal)
	if len(strVal) == 0 {
		return false
	}
	ptn,_ := regexp.Compile("\\-?(\\d+\\.\\d+|\\d+)")
	return ptn.Match([]byte(strVal))
}
