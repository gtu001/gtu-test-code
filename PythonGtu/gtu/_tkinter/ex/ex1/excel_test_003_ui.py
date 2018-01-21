from pathlib import Path
from tkinter import Text, Button, Label
from tkinter.filedialog import askopenfilename

from gtu._tkinter.util import tkinterUtil 
from gtu._tkinter.util.tkinterUIUtil import _Text 
from gtu._tkinter.util.tkinterUIUtil import _Label 
from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.openpyxl_test.ex1.janna import excel_test_rpt_36
from gtu.reflect import checkSelf
import tkinter as tk


class MainUI():
    def __init__(self):
        win = tk.Tk()
    
        win.title("產生36報表excel")
        l1 = Label(win, text="路徑:")
        l1.grid(column=0, row=0)
        
        self.text = _Text.create(win)
        self._text = _Text(self.text)
        self._text.dbclick(self.textClick)
        self.text.grid(column=1, row=0)
        
        l2 = Label(win, text="年度:")
        l2.grid(column=0, row=1)
        
        self.yyyText = _Text.create(win)
        self._yyyText = _Text(self.yyyText)
        self.yyyText.grid(column=1, row=1)
        
        self.btn = Button(win, text="執行", command=self.btnClick)
        self.btn.grid(column=2, row=0)
        
        self.label = Label(win, text="結果!")
        self._label = _Label(self.label)
        self.label.grid(column=1, row=2)
        
        win.mainloop()
        
    def textClick(self, event):
        file = Path(tkinterUtil.askopenfilename())
        if file.is_file():
            self._text.clear()
            self._text.setText(file.absolute())

    def btnClick(self):
        try:
            if self._yyyText.getText() == '' :
                tkinterUtil.error("錯誤", "請輸入完整")
                return;
            
            path = self._text.getText()
            file = Path(path)
            if fileUtil.getSubName(file) not in ("xlsx") :
                tkinterUtil.message("檔案錯誤", "檔案錯誤")
            else:
                print("開始執行")
                excel_test_rpt_36.main(self._text.getText(), self._yyyText.getText())
                tkinterUtil.message("訊息", "執行完成!")
        except Exception as ex:
            errorHandler.printStackTrace2(ex)
            tkinterUtil.error("錯誤", ex)
    

if __name__ == '__main__':
    MainUI()
