package main

import (
	"fmt"
	"strings"
)

func main() {
    fmt.Println(RemoveSpace("  dd  dd  "))
    fmt.Println(Trim("  dd  dd  "))
}

func Trim(strVal string) string {
	return strings.Trim(strVal, " ")
}

func RemoveSpace(strVal string) string {
	var resultStr string = ""
    for _, r := range strVal {
        c := string(r)
        if c != " " {
        	resultStr += c
        }
    }
	return resultStr
}