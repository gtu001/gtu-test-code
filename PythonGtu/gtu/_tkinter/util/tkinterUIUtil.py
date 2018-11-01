import os
import re
from tkinter import Text, StringVar, Checkbutton, IntVar, Radiobutton
from tkinter.constants import W
from tkinter.ttk import Combobox
from tkinter.ttk import Progressbar

from gtu._tkinter.util import tkinterUtil 
from gtu.io import fileUtil
from gtu.string import stringUtil
import tkinter as tk


class _Label():
    '''建立Label'''

    def __init__(self, label):
        self.label = label

    def setText(self, value):
        self.label.config(text=value)

    def click(self, command):
        self.label.bind("<Button-1>", command)
        
        
class _Text():
    '''建立Text'''

    def __init__(self, text):
        self.text = text

    def click(self, command):
        self.text.bind("<Button-1>", command)

    def dbclick(self, command):
        self.text.bind('<Double-1>', command) 

    def getText(self):
        str = self.text.get(1.0, "end")
        str = str.replace("\n", "")
        str = str.replace("\r", "")
        str = str.strip()
        return str

    def clear(self):
        self.text.delete('1.0', "end")

    def appendText(self, value):
        self.text.insert("end", value)

    def setText(self, value):
        _Text.set_text(self.text, value)

    @staticmethod
    def create(win):
        return Text(win, width=30, height=2, font=("Courier", 10))

    @staticmethod
    def set_text(widget, value):
        if isinstance(widget, _Text) :
            widget.setText(value)
        else :
            widget.delete('1.0', "end")
            widget.insert("end", value)
            
        
    

class _Combobox():
    '''下拉選單'''

    def __init__(self, combobox):
        self.var = StringVar()
        self.combobox = combobox
        self.combobox.configure(textvariable=self.var)

    def click(self, command):
        self.combobox.bind('<<ComboboxSelected>>', command)

    def setIndex(self, index):
        self.combobox.current(index) 

    def getValue(self):
        return self.combobox.get()

    def setList(self, lst):
        self.combobox.configure(values=lst)

    def onchange(self, func):

        def innerOnChange(name, idontknow, mode):
            func(self.combobox.get())

        self.var.trace("w", innerOnChange)
        

class _Checkbutton():
    '''checkbox'''

    def __init__(self, checkbutton):
        self.var = IntVar()
        self.checkbutton = checkbutton
        self.checkbutton.configure(variable=self.var)

    def setText(self, text):
        self.checkbutton.configure(text=text)

    def onchange(self, func):

        def innerOnChange(name, idontknow, mode):
            func(self.var.get() == 1)

        self.var.trace("w", innerOnChange)

    def isSelected(self):
        return self.var.get() == 1

    def setSelected(self, bool):
        if bool :
            self.checkbutton.select()
        else:
            self.checkbutton.deselect()
            
            
class _Radiobutton():

    def __init__(self):
        self.var = StringVar()
        self.radios = []
        self.gridLst = []

    def addRadio(self, text, value, win, row=None, column=None):
        r1 = Radiobutton(win, text=text, value=value, variable=self.var)
        self._validateGrid(row, column)
        r1.grid(row=row, column=column)
        self.radios.append(r1)

    def onchange(self, func):

        def innerOnChange(name, idontknow, mode):
            func(self.var.get())

        self.var.trace("w", innerOnChange)

    def setValue(self, val):
        self.var.set(val)

    def getValue(self):
        return self.var.get()

    def _validateGrid(self, row, column):
        key = "{},{}".format(row, column)
        if self.gridLst.__contains__(key):
            raise Exception("位置已存在元素 row:{},column{}".format(row, column))
        self.gridLst.append(key)
        
        
class TextSetPathEventHandler():
    '''
    設定 text 雙擊設路徑的 event
    '''
    def __init__(self, textWidget, isFile=True, nameFormat="", ignoreCase=False):
        self.textWidget = textWidget
        self.isFile = isFile
        self.nameFormat = nameFormat
        self.ignoreCase = ignoreCase
        
        if isinstance(self.textWidget, _Text) :
            self.textWidget.dbclick(self.setPathEvent)
        else :
            self.textWidget.bind('<Double-1>', self.setPathEvent) 
            
    def checkPathFormatValid(self, path):
        fileName = fileUtil.getName(path)
        print("檢查檔名format : ", self.nameFormat, ", 檔名 : ", fileName)
        
        flag = 0
        if self.ignoreCase :
            flag = re.I
        
        ptn = re.compile(self.nameFormat, flag)
        mth = ptn.search(fileName)
        if mth is None :
            return False
        return True

    def setPathEvent(self, event):
        try:
            path = None
            if self.isFile :
                path = tkinterUtil.askopenfilename()
            else :
                path = tkinterUtil.askdirectory()
                
            if not path or not os.path.exists(path) :
                tkinterUtil.message("路徑有誤", "路徑不存在 :" + path)
                return;
            
            if self.isFile :
                if not os.path.isfile(path) :
                    tkinterUtil.message("路徑有誤", "路徑非檔案 :" + path)
                    return;
            else :
                if not os.path.isdir(path) :
                    tkinterUtil.message("路徑有誤", "路徑非檔目錄:" + path)
                    return;
            
            #驗證檔名
            if stringUtil.isNotBlank(self.nameFormat) and not self.checkPathFormatValid(path) :
                tkinterUtil.message("路徑有誤", "檔名格式必須為 : " + self.nameFormat)
                return;
            
            _Text.set_text(event.widget, path)
        except Exception as ex:
            tkinterUtil.error_ex(ex, ex)


class _ProgressBar():
    def __init__(self, gui, length=200):
        self.progress = Progressbar(gui, orient="horizontal",
                                        length=length, mode="determinate",
                                        style='yellow.Vertical.TProgressbar')
        
    def grid(self, **args):
        self.progress.grid(args)

    def pack(self):
        self.progress.pack(fill='both',expand=True,side='top')
        
    def setupValue(self, initVal, maxVal):
        self.progress["value"] = initVal
        self.progress["maximum"] = maxVal
        print("setupValue ", initVal, maxVal)
        
    def start(self, interval=None):
        ''' 
                    啟動自動累加模式
        interval 刷新間隔 , 預設  50 milliseconds
        '''
        self.progress.start(interval)

    def step(self, amount=None):
        '''
        step 值預設amount = 1
        '''
        self.progress.step(amount)

    def stop(self):
        self.progress.stop()
    


class _Button():
    
    @staticmethod
    def runInThread(actionFunc, exceptionFunc=None, finallyFunc=None):
        '''
        開條 thread 執行 處理
        '''
        def returnFunc():
            import _thread
            def process() :
                try:
                    actionFunc()
                except Exception as ex:
                    tkinterUtil.error_ex(ex, ex)
                    if exceptionFunc is not None :
                        exceptionFunc()
                finally :
                    if finallyFunc is not None :
                        finallyFunc()
                    pass
            _thread.start_new(process, ())
        
        return returnFunc
    
    @staticmethod
    def enable(button):
        button.config(state="normal")
        
    @staticmethod
    def disable(button):
        button.config(state="disable")


if __name__ == '__main__' :
    root = tk.Tk()
    
    pbar = _ProgressBar(root)
    pbar.setupValue(0, 100) 
    pbar.grid(column=0, row=0)
    pbar.step(25)
    
    root.mainloop()
    
