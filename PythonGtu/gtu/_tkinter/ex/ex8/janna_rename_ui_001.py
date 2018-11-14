import _thread
from enum import Enum
import io
import os
from pathlib import Path
import re
from tkinter import Text, Button, Label
from tkinter.filedialog import askopenfilename

from gtu._tkinter.util import tkinterUtil, tkinterUIUtil
from gtu._tkinter.util.tkinterUIUtil import TextSetPathEventHandler 
from gtu._tkinter.util.tkinterUIUtil import _Label , _ProgressBar, _Scrollbar
from gtu._tkinter.util.tkinterUIUtil import _Text , _Button, _TabFrame
from gtu.io import fileUtil
from gtu.string import stringUtil
import tkinter as tk


class MainUI():
    
    def __init__(self):
        win = tk.Tk()
        win.title("修改檔名")
        
        rootTab = _TabFrame(win)
        tab1 = rootTab.createTab("路徑設定")
        
        #------------------------------row 1
        
        l1 = _Label(root=tab1)
        l1.setText("設定目錄:")
        l1.grid(column=0, row=0)
        
        self.dirText = _Text(_Text.create(tab1))
        self.dirText.grid(column=1, row=0)
        TextSetPathEventHandler(self.dirText, False, r"", True)
        
        #------------------------------row 3
        
        self.pbar = _ProgressBar(tab1, length=300)
        self.pbar.grid(column=1, row=2)
        
        self.btn2 = _Button(root=tab1)
        self.btn2.setText("修改檔名")
        self.btn2.command(_Button.runInThread(
                                            self.executeBtnAction,
                                            exceptionFunc=self.executeBtnAction_finally,
                                            finallyFunc=self.executeBtnAction_finally
                                            ))
        self.btn2.grid(column=2, row=2)
        
        #------------------------------row End
        
        tab2 = rootTab.createTab("錯誤訊息")
        
        #------------------------------row 1
        
        self.logText = _Text(root=tab2)
        self.logText.grid(column=0, row=0)
#         self.logText.place(width=400, height=60)
        
        logTextScroll = _Scrollbar(self.logText.text, tab2)
        logTextScroll.applyX()
        logTextScroll.applyY()
        logTextScroll.grid(0, 0)
        
        #------------------------------row End
        
        tkinterUtil.centerWin(win)
        win.mainloop()
    
    
    def executeBtnAction(self):
        self.btn2.disable()
        self.logText.clear()
        
        rename = RenameHandler(self.dirText.getText())
        
        tkinterUtil.message("產生結果", "成功!!")
        
    def executeBtnAction_finally(self):
        self.btn2.enable()
        
        
class RenameHandler():
    
    ptn1 = re.compile(r"TX\w+\_(?P<rptName>R\w+?)\_\d+\-(?P<type>[ic])", re.I)
    
    def __init__(self, dirpath):
        from os import walk
        import os
        for (dirpath, dirnames, filenames) in walk(dirpath):
            for (i, name) in enumerate(filenames) :
                old_file = os.path.join(dirpath, name)
                print(i, old_file)
                
                mth = RenameHandler.ptn1.search(name)
                if mth :
                    rptName = mth.group("rptName")
                    type = mth.group("type") 
                    
                    print("rptName", rptName, type)
#             old_file = os.path.join("directory", "a.txt")
#             new_file = os.path.join("directory", "b.kml")
#             os.rename(old_file, new_file)


if __name__ == '__main__':
    MainUI()
    print("done..")

