package main

import (
	"fmt"
	//"strings"
    "regexp"
    //"reflect"
)

func tester() {
	r := &DoReplace{}
    r.Replace = func(str string) string {
    	fmt.Println(str)
    	return "@"
    }
    fmt.Println("結果 : ", MatchReplace("  dfasdfl  lksjdf  sldfjsdlf   ", "d+", r))
    fmt.Println("done...")
}

type DoReplace struct {
	count int
	Replace func(string) string
}

func MatchReplace(strVal string, ptnStr string, r *DoReplace) string {
	ptn,_ := regexp.Compile(ptnStr)
	intArry := ptn.FindAllIndex ([]byte(strVal), -1)
	if len(intArry) == 0 {
		return strVal
	}
	rtnString := ""
	startPos := 0
	for _,subArry := range intArry {
		rtnString += strVal[startPos : subArry[0]]
		rtnString += r.Replace(strVal[subArry[0] : subArry[1]])
		startPos = subArry[1]
	}
	rtnString += strVal[startPos:]
	return rtnString
}