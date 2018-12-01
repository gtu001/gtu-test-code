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
        
        self.rootTab = _TabFrame(win)
        tab1 = self.rootTab.createTab("路徑設定")
        
        #------------------------------row 0
        
        l0 = _Label(root=tab1)
        l0.setText("設定預設目錄:")
        l0.grid(column=0, row=0)
        
        self.defText = _Text(_Text.create(tab1))
        self.defText.grid(column=1, row=0)
        self.defTextHandler = TextSetPathEventHandler(self.defText, isFile=False, nameFormat=r"", ignoreCase=True)
        
        #------------------------------row 1
        
        l1 = _Label(root=tab1)
        l1.setText("Cognus表路徑:")
        l1.grid(column=0, row=1)
        
        self.rptCText = _Text(_Text.create(tab1))
        self.rptCText.grid(column=1, row=1)
        self.rptCTextHandler = TextSetPathEventHandler(self.rptCText, True, r".*\.xlsx", True)
        
        #------------------------------row 2
        
        l2 = _Label(root=tab1)
        l2.setText("IText表路徑:")
        l2.grid(column=0, row=2)
        
        self.rptIText = _Text(_Text.create(tab1))
        self.rptIText.grid(column=1, row=2)
        self.rptITextHandler = TextSetPathEventHandler(self.rptIText, True, r".*\.xlsx", True)
        
        #------------------------------row 3
        
        self.pbar = _ProgressBar(tab1, length=300)
        self.pbar.grid(column=1, row=3)
        
        self.btn2 = _Button(root=tab1)
        self.btn2.setText("產生比對表")
        self.btn2.command(_Button.runInThread(
                                            self.executeBtnAction,
                                            exceptionFunc=self.executeBtnAction_finally,
                                            finallyFunc=self.executeBtnAction_finally
                                            ))
        self.btn2.grid(column=2, row=3)
        
        #------------------------------row End
        
        tab2 = self.rootTab.createTab("錯誤訊息")
        
        #------------------------------row 1
        
        self.logText = _Text(root=tab2)
        self.logText.grid(column=0, row=0)
#         self.logText.place(width=400, height=60)
        
        logTextScroll = _Scrollbar(self.logText.text, tab2)
        logTextScroll.applyX()
        logTextScroll.applyY()
        logTextScroll.grid(0, 0)
        
        #------------------------------row End
        
        #------------------------------------common setting
        def func1(path):
            print("----", path)
            self.rptITextHandler.args['initialdir'] = path
            self.rptITextHandler.resetEvent()
            self.rptCTextHandler.args['initialdir'] = path
            self.rptCTextHandler.resetEvent()
        self.defTextHandler.func = func1
        #------------------------------------common setting
        
        tkinterUtil.centerWin(win)
        win.mainloop()
    
    
    def executeBtnAction(self):
        self.btn2.disable()
        self.logText.clear()
        fileC = self.rptCText.getText()
        fileI = self.rptIText.getText()
        excel_test_rpt_8110_cellCompare.mainDetail(fileC, fileI, self.pbar, self.logText)
        tkinterUtil.message("產生結果", "成功!!")
        
        
    def executeBtnAction_finally(self):
        self.rootTab.selectTab(1)
        self.btn2.enable()


if __name__ == '__main__':
    MainUI()
    print("done..")

