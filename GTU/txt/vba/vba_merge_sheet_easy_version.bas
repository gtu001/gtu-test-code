Attribute VB_Name = "Module1"


Sub main()
    Application.ScreenUpdating = False

    Dim s As Worksheet, t As String
    Dim i As Long, K As Long
    K = Sheets.Count
    For i = K To 1 Step -1
        t = Sheets(i).Name
        If t = "test" Then
            Application.DisplayAlerts = False
                Sheets(i).Delete
            Application.DisplayAlerts = True
        End If
    Next i
    
    Dim allSht As Worksheet
    Set allSht = Worksheets.Add(After:=Worksheets(Worksheets.Count))
    allSht.Name = "test"
    
    
    Dim r As Integer
    
    Dim totalRowSize As Integer
    
    totalRowSize = 0
    
    For isht = 1 To Worksheets.Count - 1
        If Sheets(isht).Name = "年中人口" Then
            GoTo ContinueLoop
        End If
    
        Sheets(isht).Select
        
        Dim cnt As Integer
        r = Sheets(isht).UsedRange.Rows.Count
        r = Sheets(isht).UsedRange.Rows(r).row
        
        Dim a As Range
        Set a = Range("A1", Cells.Find(What:="*", _
            After:=Range("A1"), _
            LookAt:=xlPart, _
            LookIn:=xlFormulas, _
            searchorder:=xlByRows, _
            searchdirection:=xlPrevious, _
            MatchCase:=False) _
        )
        a.Copy
        a.PasteSpecial xlPasteValues
        
        Sheets("test").Select
        Sheets("test").Paste
        
        totalRowSize = totalRowSize + r
        
        Worksheets("test").Activate
        Worksheets("test").Range("A1").Select
        ActiveCell.Offset(rowOffset:=row, columnOffset:=col).Activate
        
        
        Debug.Print ("current sheet " & Sheets(isht).Name)
        
ContinueLoop:
    Next isht
    
    Debug.Print ("done..")
    
    
End Sub


