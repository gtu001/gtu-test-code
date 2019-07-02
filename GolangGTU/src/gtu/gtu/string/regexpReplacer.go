package main

import (
	//"fmt"
	//"strings"
    "regexp"
    //"reflect"
)


type DoReplace struct {
	Replace func(string) string
}

func MatchReplace(strVal string, ptnStr string, r DoReplace) string {
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