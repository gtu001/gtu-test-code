package main

import (
	"fmt"
	"strings"
    "regexp"
    "reflect"
    //"strconv"
)

func main() {
    fmt.Println("TrimAllSpace-->" , trimAllSpace("  dd  dd  "))
    fmt.Println("Trim-->" , trim("  dd  dd  "))
    fmt.Println("GetChinese-->" , getChinese("  dfasdfl 測試 lksjdf 中文 sldfjsdlf   "))
    fmt.Println("HasChinese-->" , hasChinese("  dfasdfl 測試 lksjdf 中文 sldfjsdlf   "))
    fmt.Println("HasChinese-->" , hasChinese("  dfasdfl  lksjdf  sldfjsdlf   "))
    fmt.Println("Left Padding-->" , padding("aaa", 10, 'x', true))
    fmt.Println("Right Padding-->" , padding("aaa", 10, 'x', false))
    fmt.Println("IsNumber-->" , isNumber(-12354.566))
   // tester()
}

func trim(strVal string) string {
	return strings.Trim(strVal, " ")
}

func trimAllSpace(strVal string) string {
	var resultStr string = ""
    for _, r := range strVal {
        c := string(r)
        if c != " " {
        	resultStr += c
        }
    }
	return resultStr
}

func typeOf(value interface{}) string {
	return fmt.Sprintf("%v", reflect.TypeOf(value))
}

func valueOf(value interface{}) string {
	return fmt.Sprintf("value:[%v], type:[%T]", value, value)
}

func getChinese(strVal string) string {
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

func hasChinese(strVal string) bool {
	return len(getChinese(strVal)) > 0
}

func padding(strVal string, length int, c rune, isLeft bool) string {
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

func isNumber(dataVal interface{}) bool {
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
