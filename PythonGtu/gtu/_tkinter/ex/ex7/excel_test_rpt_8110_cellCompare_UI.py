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
from gtu._tkinter.util.tkinterUIUtil import _Label , _ProgressBar
from gtu._tkinter.util.tkinterUIUtil import _Text , _Button
from gtu.decode import decodeUtil
from gtu.enum import enumUtil
from gtu.enum.enumUtil import EnumHelper
from gtu.error import errorHandler
from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.openpyxl_test.ex1.janna import excel_test_rpt_8110_cellCompare
from gtu.os import runtimeUtil
from gtu.reflect import checkSelf
from gtu.string import stringUtil
import tkinter as tk


class MainUI():
    
    def __init__(self):
        win = tk.Tk()
        win.title("8110報表比對")
        #------------------------------row 1
        
        l1 = Label(win, text="C表路徑:")
        l1.grid(column=0, row=0)
        
        self.rptCText = _Text.create(win)
        self.rptCText.grid(column=1, row=0)
        self._rptCText = _Text(self.rptCText)
        TextSetPathEventHandler(self._rptCText, True, r".*\.xlsx", True)
        
        #------------------------------row 2
        
        l2 = Label(win, text="I表路徑:")
        l2.grid(column=0, row=1)
        
        self.rptIText = _Text.create(win)
        self.rptIText.grid(column=1, row=1)
        self._rptIText = _Text(self.rptIText)
        TextSetPathEventHandler(self._rptIText, True, r".*\.xlsx", True)
        
        #------------------------------row 3
        
        self.pbar = _ProgressBar(win, length=300)
        self.pbar.grid(column=1, row=2)
        
        self.b2 = Button(win, text="產生比對表", command=_Button.runInThread(
                                                            self.executeBtnAction,
                                                            exceptionFunc=self.executeBtnAction_finally,
                                                            finallyFunc=self.executeBtnAction_finally
                                                            )
                                                        )
        self.b2.grid(column=2, row=2)
        
        #------------------------------row End
        
        tkinterUtil.centerWin(win)
        win.mainloop()
    
    def executeBtnAction(self):
        _Button.disable(self.b2)
        fileC = self._rptCText.getText()
        fileI = self._rptIText.getText()
        excel_test_rpt_8110_cellCompare.mainDetail(fileC, fileI, self.pbar)
        tkinterUtil.message("產生結果", "成功!!")
        
    def executeBtnAction_finally(self):
        _Button.enable(self.b2)


if __name__ == '__main__':
    MainUI()
    print("done..")

