package main

import (
	"fmt"
	"strings"
    "regexp"
    "reflect"
)

func main() {
    fmt.Println(TrimAllSpace("  dd  dd  "))
    fmt.Println(Trim("  dd  dd  "))
    fmt.Println(GetChinese("  dfasdfl 測試 lksjdf 中文 sldfjsdlf   "))
    fmt.Println(HasChinese("  dfasdfl 測試 lksjdf 中文 sldfjsdlf   "))
    fmt.Println(HasChinese("  dfasdfl  lksjdf  sldfjsdlf   "))
    
    tester()
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

