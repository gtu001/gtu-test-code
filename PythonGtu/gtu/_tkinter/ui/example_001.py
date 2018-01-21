from pathlib import Path
from tkinter import Text, Button, Label
from tkinter.filedialog import askopenfilename

from gtu._tkinter.util import tkinterUIUtil
from gtu._tkinter.util import tkinterUtil 
from gtu._tkinter.util.tkinterUIUtil import _Text 
from gtu.io import fileUtil
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
        
        self.btn = Button(win, text="執行", command=self.btnClick)
        self.btn.grid(column=2, row=0)
        
        self.label = Label(win, text="結果!")
        self._label = tkinterUIUtil._Label(self.label)
        self.label.grid(column=1, row=1)
        
        win.mainloop()

    def btnClick(self):
        path = self._text.getText().replace("\n", "")
        file = Path(path)
        if fileUtil.getSubName(file) in ("xlsx") :
            print("OK")
        else:
            tkinterUtil.message("檔案錯誤", "檔案錯誤")
        
    def textClick(self, event):
        file = Path(tkinterUtil.askopenfilename())
        if file.is_file():
            self._text.clear()
            self._text.setText(file.absolute())

if __name__ == '__main__':
    MainUI()
