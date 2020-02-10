ireport_決定欄位是否顯示
---
  在"List(component) - 屬性"視窗
  找 Print When Expressioin
    輸入 new Boolean(!$F{bankDisplay_D00}.equalsIgnoreCase('Y'))
    
