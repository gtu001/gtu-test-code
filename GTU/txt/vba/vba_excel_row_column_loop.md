vba_excel_row_column_loop.md
---

Sub CaculateWorkingDay()

    Dim rangeStr As String
    rangeStr = prompt("輸入範圍", "輸入範圍", "D24:AH27")  '游松青
    
    
    Dim columCnt As Integer
    Dim rowCnt As Integer
    Dim rng As Range
    Set rng = Worksheets(1).Range(rangeStr)
    
    Dim columnStart As Integer
    Dim columnEnd As Integer
    Dim rowStart As Integer
    Dim rowEnd As Integer
    
    columnStart = getColumnStartEnd(rng, True)
    columnEnd = getColumnStartEnd(rng, False)
    rowStart = getRowStartEnd(rng, True)
    rowEnd = getRowStartEnd(rng, False)

    Dim hasWorkCount As Integer
    hasWorkCount = 0
    
    For c = columnStart To columnEnd
        Dim bool As Boolean
        bool = False
        For r = rowStart To rowEnd
            Debug.Print CStr(c) + " " + CStr(r) + " " + CStr(rng.Cells(r, c).Value)
            Dim strVal As String
            strVal = Trim(CStr(rng.Cells(r, c).Value))
            If strVal <> "" Then
                bool = True
            End If
        Next
        If bool Then
            hasWorkCount = hasWorkCount + 1
            Debug.Print "有工作..."
        End If
    Next

    MsgBox "有工作數累積為 " + CStr(hasWorkCount)
End Sub

Private Function getColumnStartEnd(rng1 As Range, isStart As Boolean)
    Dim max1 As Integer
    Dim min1 As Integer
    max1 = -9999
    min1 = 9999
    For Each r In rng1.Columns
        'Debug.Print TypeName(r) + " " + CStr(r.Column)
        max1 = WorksheetFunction.max(max1, CInt(r.Column))
        min1 = WorksheetFunction.Min(min1, CInt(r.Column))
    Next r
    Debug.Print "Range Column " + CStr(min1) + " , " + CStr(max1)
    If isStart Then
        getColumnStartEnd = min1
    Else
        getColumnStartEnd = max1
    End If
End Function


Private Function getRowStartEnd(rng1 As Range, isStart As Boolean)
    Dim max1 As Integer
    Dim min1 As Integer
    max1 = -9999
    min1 = 9999
    For Each r In rng1.Rows
        'Debug.Print TypeName(r) + " " + CStr(r.Row)
        max1 = WorksheetFunction.max(max1, CInt(r.Row))
        min1 = WorksheetFunction.Min(min1, CInt(r.Row))
    Next r
    Debug.Print "Range Row " + CStr(min1) + " , " + CStr(max1)
    If isStart Then
        getRowStartEnd = min1
    Else
        getRowStartEnd = max1
    End If
End Function


Private Function prompt(title As String, message As String, defaultVal As String)
    Dim a As Variant
    Dim b As String
    a = InputBox(message, title, defaultVal, 700, 600)
    b = CStr(a)
    prompt = b
End Function


Private Function showType(obj As Variant)
    Debug.Print TypeName(obj)
End Function


' 取得sheet 兩種方法 Worksheets(1) or Worksheets("工作表1")
