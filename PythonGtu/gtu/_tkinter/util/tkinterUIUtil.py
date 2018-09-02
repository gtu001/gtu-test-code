from tkinter import Text, StringVar, Checkbutton, IntVar, Radiobutton
from tkinter.constants import W
from tkinter.ttk import Combobox

import tkinter as tk


class _Label():
    '''建立Label'''
    def __init__(self, label):
        self.label = label
    def setText(self, value):
        self.label.config(text = value)
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
    def setText(self, value):
        self.text.insert("end", value)
    @staticmethod
    def create(win):
        return Text(win, width=30, height=2, font=("Courier", 10))


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
        

if __name__ == '__main__' :
    root = tk.Tk()
    root.mainloop()
    
    
    
    
    
    
    