from pathlib import Path
from tkinter import Text, Button, Label
from tkinter.filedialog import askopenfilename

from gtu._tkinter.util import tkinterUtil 
from gtu._tkinter.util.tkinterUIUtil import _Text 
from gtu._tkinter.util.tkinterUIUtil import _Label 
from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.openpyxl_test.ex1.janna import excel_test_rpt_37
from gtu.reflect import checkSelf
import tkinter as tk
from gtu._tkinter.util import tkinterUtil
from gtu.openpyxl_test.ex2 import excel_two_sheet_compare_001 as t
from openpyxl.workbook.workbook import Workbook
from gtu.error import errorHandler


class MainUI():

    def __init__(self):
        win = tk.Tk()
        win.title("產生excel sheet比對")
        
        #row 0 ---------------
        l1 = Label(win, text="excel1路徑:")
        l1.grid(column=0, row=0)
        
        self.excel1Text = _Text.create(win)
        self.excel1Text.grid(column=1, row=0)
        self._excel1Text = _Text(self.excel1Text)
        self._excel1Text.dbclick(self.excel1TextClick)
        
        l1_2 = Label(win, text="sheet名:")
        l1_2.grid(column=2, row=0)
        
        self.sheetNameText1 = _Text.create(win)
        self.sheetNameText1.grid(column=3, row=0)
        self._sheetNameText1 = _Text(self.sheetNameText1)
        
        #row 1 ---------------
        l2 = Label(win, text="excel2路徑:")
        l2.grid(column=0, row=1)
        
        self.excel2Text = _Text.create(win)
        self.excel2Text.grid(column=1, row=1)
        self._excel2Text = _Text(self.excel2Text)
        self._excel2Text.dbclick(self.excel2TextClick)
        
        l2_2 = Label(win, text="sheet名:")
        l2_2.grid(column=2, row=1)
        
        self.sheetNameText2 = _Text.create(win)
        self.sheetNameText2.grid(column=3, row=1)
        self._sheetNameText2 = _Text(self.sheetNameText2)
        
        #row 2 ---------------
        l2 = Label(win, text="產出檔名:")
        l2.grid(column=0, row=2)
        
        self.excel3Text = _Text.create(win)
        self.excel3Text.grid(column=1, row=2)
        self._excel3Text = _Text(self.excel3Text)
        
        #row 3 ---------------
        
        b1 = Button(win, text="執行", command=self.executeBtnAction)
        b1.grid(column=1, row=3)
        
        win.mainloop()
        
        
    def excel1TextClick(self, event):
        path = tkinterUtil.askopenfilename()
        path2 = Path(path)
        if path2.is_file() :
            self._excel1Text.setText(path2.absolute())
            
            
    def excel2TextClick(self, event):
        path = tkinterUtil.askopenfilename()
        path2 = Path(path)
        if path2.is_file() :
            self._excel2Text.setText(path2.absolute())
            
            
    def executeBtnAction(self):
        try:
            sheetName1 = self._sheetNameText1.getText()
            sheetName2 = self._sheetNameText2.getText()
            
            excel1Lst = t.getCellLst(self._excel1Text.getText(), sheetName1)
            excel2Lst = t.getCellLst(self._excel2Text.getText(), sheetName2)
            
            print("excel1Lst", excel1Lst)
            print("excel2Lst", excel2Lst)
                    
            wb = Workbook()
            
            t.cloneSheetToTargetWb(wb, "來自1", excel1Lst)
            t.cloneSheetToTargetWb(wb, "來自2", excel2Lst)
            
            (maxRow, maxCol) = t.getMaxPos(excel1Lst);
            
            t.createCompareSheet(wb, ("來自1", "來自2"), (maxRow, maxCol))
            
            toExcel = fileUtil.getDesktopDir() + self._excel3Text.getText()
            wb.save(toExcel)
        except Exception as ex:
            tkinterUtil.error_ex(ex, ex)
            

if __name__ == '__main__':
    MainUI()
