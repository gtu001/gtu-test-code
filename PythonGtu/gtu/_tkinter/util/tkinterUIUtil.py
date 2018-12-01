import os
import re
from tkinter import Text, StringVar, Checkbutton, IntVar, Radiobutton, ttk
from tkinter.constants import W
from tkinter.ttk import Combobox, Label, Button, Scrollbar
from tkinter.ttk import Progressbar

from tkinter.scrolledtext import ScrolledText
from IPython.terminal.pt_inputhooks import tk

from gtu._tkinter.util import tkinterUtil 
from gtu.io import fileUtil
from gtu.reflect import checkSelf
from gtu.string import stringUtil
import tkinter as tk


class BaseUI():

    def __init__(self, widget):
        self.widget = widget
        
    def grid(self, column, row, **args):
        self.widget.grid(column=column, row=row, **args)
        
    def pack(self, **args):
        self.widget.pack(**args)
        
    def place(self, x=None, y=None, width=None, height=None):
        self.widget.place(x=x, y=y, width=width, height=height)
        
    def gridInfo(self):
        return self.widget.grid_info()


class _Label(BaseUI):
    '''建立Label'''

    def __init__(self, label=None, root=None):
        if label is None :
            label = Label(root)
        self.label = label
        BaseUI.__init__(self, self.label)

    def setText(self, value):
        self.label.config(text=value)

    def click(self, command):
        self.label.bind("<Button-1>", command)
        
        
class _Text(BaseUI):
    '''建立Text'''

    def __init__(self, text=None, root=None):
        if text is None :
            text = Text(root)
        self.text = text
        BaseUI.__init__(self, self.text)

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
    def create(win, scrollable=False):
        if not scrollable :
            text = Text(win, width=30, height=2)
        else :
            text = ScrolledText(win)
        text.config(font=("Courier", 10), undo=True, wrap='word')
        return text

    @staticmethod
    def set_text(widget, value):
        if isinstance(widget, _Text) :
            widget.setText(value)
        else :
            widget.delete('1.0', "end")
            widget.insert("end", value)
    

class _Combobox(BaseUI):
    '''下拉選單'''

    def __init__(self, combobox=None, root=None):
        if combobox is None :
            combobox = Combobox(root)
        self.var = StringVar()
        self.combobox = combobox
        self.combobox.configure(textvariable=self.var)
        BaseUI.__init__(self, self.combobox)

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
        

class _Checkbutton(BaseUI):
    '''checkbox'''

    def __init__(self, checkbutton=None, root=None):
        if checkbutton is None :
            checkbutton = Checkbutton(root)
        self.var = IntVar()
        self.checkbutton = checkbutton
        self.checkbutton.configure(variable=self.var)
        BaseUI.__init__(self, self.checkbutton)

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

    def __init__(self, textWidget, isFile=True, nameFormat="", ignoreCase=False, initialdir=None, initialfile=None, filetypes=None, func=None):
        self.textWidget = textWidget
        self.isFile = isFile
        self.nameFormat = nameFormat
        self.ignoreCase = ignoreCase
        self.args = {
            "initialdir" : initialdir,
            "initialfile" : initialfile,
            "filetypes" : filetypes,
            }
        self.func = func
        self.resetEvent()
    
    def resetEvent(self):
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
                print("**self.args", self.args)
                path = tkinterUtil.askopenfilename(**self.args)
            else :
                print("**self.args", self.args)
                path = tkinterUtil.askdirectory(**self.args)
                
            if not path or not os.path.exists(path) :
                tkinterUtil.message("路徑有誤", "路徑不存在 :" + str(path))
                return;
            
            if self.isFile :
                if not os.path.isfile(path) :
                    tkinterUtil.message("路徑有誤", "路徑非檔案 :" + str(path))
                    return;
            else :
                if not os.path.isdir(path) :
                    tkinterUtil.message("路徑有誤", "路徑非檔目錄:" + str(path))
                    return;
            
            # 驗證檔名
            if stringUtil.isNotBlank(self.nameFormat) and not self.checkPathFormatValid(path) :
                tkinterUtil.message("路徑有誤", "檔名格式必須為 : " + str(self.nameFormat))
                return;
            
            _Text.set_text(event.widget, path)
            if self.func :
                self.func(path)
        except Exception as ex:
            tkinterUtil.error_ex(ex, ex)


class _ProgressBar(BaseUI):

    def __init__(self, gui, length=200):
        self.progress = Progressbar(gui, orient="horizontal",
                                        length=length, mode="determinate",
                                        style='yellow.Vertical.TProgressbar')
        BaseUI.__init__(self, self.progress)

    def pack(self):
        self.progress.pack(fill='both', expand=True, side='top')
        
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


class _Button(BaseUI):
    
    def __init__(self, button=None, root=None):
        if button is None :
            self.button = Button(root)
        BaseUI.__init__(self, self.button)
        
    def setText(self, text):
        self.button.configure(text=text)
    
    def command(self, func):
        self.button.configure(command=func)
        
    def enable(self):
        self.button.config(state="normal")
        
    def disable(self):
        self.button.config(state="disable")
    
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


class _TabFrame():

    def __init__(self, rootUI, height=None):
        self.nb = ttk.Notebook(rootUI)
        self.universal_height = height
        self.nb.grid(column=0)
    
    def createTab(self, title, width=None):
        tab = ttk.Frame(self.nb, width=width, height=self.universal_height)
        self.nb.add(tab, text=title)
        return tab
    
    def selectTab(self, tabIndex):
        self.nb.select(tabIndex)
    
    
class _Scrollbar():
    
    def __init__(self, widget, root):
        self.widget = widget
        self.root = root
        self.scrollbarX = None
        self.scrollbarY = None

    def applyY(self):
        scrollbar = Scrollbar(self.root, orient='vertical', takefocus='no')
        self.scrollbarY = scrollbar
        
        self.widget.configure(yscrollcommand=scrollbar.set)
        scrollbar.config(command=self.widget.yview)
        
    def applyX(self):
        scrollbar = Scrollbar(self.root, orient='horizontal', takefocus='no')
        self.scrollbarX = scrollbar
        
        self.widget.configure(xscrollcommand=scrollbar.set)
        scrollbar.config(command=self.widget.xview)
        # 取消自動換行
        if isinstance(self.widget, Text) :
            self.widget.config(wrap='none')
        
    def grid(self, column=None, row=None):
        if column is None :
            column = self.widget.grid_info()['column']
        if row is None :
            row = self.widget.grid_info()['row']

        print("grid column=", column, ", row=", row)
        
        if self.scrollbarX is not None :
            self.scrollbarX.grid(column=column, row=row + 1, sticky='WE')
            self.widget.grid_columnconfigure(0, weight=1)  # 沒特別效果

        if self.scrollbarY is not None :
            self.scrollbarY.grid(column=column + 1, row=row, sticky='NS')
            self.widget.grid_rowconfigure(0, weight=1)  # 沒特別效果
    
    def pack(self):
        if self.scrollbarX is not None :
            self.scrollbarX.pack(side='bottom', fill='x', anchor='w')
        if self.scrollbarY is not None :
            self.scrollbarY.pack(side='right', fill='y')  


if __name__ == '__main__' :
    root = tk.Tk()

    root.title("Testing Bot")
    
    rTab = _TabFrame(root, 606)
    
    page1 = rTab.createTab("one", 800)
    page2 = rTab.createTab("Two", 800)
    
    b1 = _Button(root=page1)
    def func():
        rTab.selectTab(1)
        
    b1.command(func)
    b1.grid(column=0,row=0)
    
    
    txt = _Text(root=page1)
    
    root.mainloop()
    
