from pathlib import Path
from tkinter import Text, Button, Label
from tkinter.filedialog import askopenfilename

from gtu._tkinter.util import tkinterUtil 
from gtu._tkinter.util.tkinterUIUtil import _Text 
from gtu._tkinter.util.tkinterUIUtil import _Label 
from gtu.error import errorHandler
from gtu.io import fileUtil
from gtu.reflect import checkSelf
import tkinter as tk
from gtu._tkinter.util import tkinterUtil
from gtu.error import errorHandler
from gtu.string import stringUtil
from gtu.os import runtimeUtil
from gtu.decode import decodeUtil
import re
import io




class MainUI():

    def __init__(self):
        win = tk.Tk()
        win.title("kill port pid")
        
        l1 = Label(win, text="輸入port:")
        l1.grid(column=0, row=0)
        
        self.dirText = _Text.create(win)
        self.dirText.grid(column=2, row=0)
        self._text1 = _Text(self.dirText)
        
        b1 = Button(win, text="執行", command=self.executeBtnAction)
        b1.grid(column=3, row=0)
        
        tkinterUtil.centerWin(win)
        win.mainloop()
            
    def executeBtnAction(self):
        try:
            portNumberText = self._text1.getText();
            if not stringUtil.isNumber(portNumberText) :
                tkinterUtil.error("非數值", "非數值 :" + portNumberText)
                return
            
            portNumberText = int(portNumberText)
            windowkillPortPid(portNumberText)
        except Exception as ex:
            tkinterUtil.error_ex(ex, ex)
            
            
def windowkillPortPid(portNumber):
    pidMap = dict()
    
    portUtil = _CloseDamnPort()
    pidSet = portUtil.getPortPid(portNumber)
    
    if pidSet is None :
        tkinterUtil.error("錯誤", "無此程序!")
        return
    
    for (i, pid) in enumerate(pidSet) :
        exeSet = portUtil.getExeFromPid(pid)
        if len(exeSet) != 0 :
            pidMap[pid] = exeSet.pop()

    firstKey = pidMap.keys().__iter__().__next__()
    answer = tkinterUtil.prompt("請決定要kill哪個", str(pidMap), firstKey)
    
    if not stringUtil.isNumber(answer) :
        tkinterUtil.error("非數值", "非數值 :" + answer)
        return
    
    out = portUtil.killDamnPid(answer)
    tkinterUtil.message("結果", out)
    
    

class _CloseDamnPort():
    BIG5 = decodeUtil.getChsCodeType(0)
    
    def __init__(self):
        pass
    
    def getPortPid(self, portNumber):
        chkPortPidStr = "netstat -ano | findstr {}".format(portNumber)
        (out, err, errcode) = runtimeUtil.runtimeExec3(chkPortPidStr, _CloseDamnPort.BIG5)
        
        if errcode != 0:
            tkinterUtil.error("錯誤", "執行錯誤!" + err);
            return
        
        pidSet = set()
        ptn = re.compile(r"[^\s]+", re.I)
        f = io.StringIO(out)
        for i, line in enumerate(f, 0):
            mth = ptn.findall(line)
            pidSet.add(int(mth[-1]))
        f.close()
        
        return pidSet
        
        
    def getExeFromPid(self, pid):
        exeSet = set()
        
        chkPidExeStr = "tasklist /fi \"pid eq {}\"".format(pid)
        (out, err, errcode) = runtimeUtil.runtimeExec3(chkPidExeStr, _CloseDamnPort.BIG5)
    
        if errcode != 0:
            tkinterUtil.error("錯誤", "執行錯誤!" + err);
            
        startPos = -1

        ptn = re.compile(r"[^\s]+", re.I)
        f = io.StringIO(out)
        for i, line in enumerate(f, 0):
            mth = ptn.findall(line)
            if len(mth) !=0 and mth[0].find("======") != -1 :
                startPos = i
            if len(mth) !=0 and i > startPos and startPos != -1:
                exeSet.add(mth[0])
        f.close()
        
        return exeSet
    
    
    def killDamnPid(self, pid):
        chkPidExeStr = "Taskkill /PID {} /F".format(pid)
        (out, err, errcode) = runtimeUtil.runtimeExec3(chkPidExeStr, _CloseDamnPort.BIG5)
        if errcode != 0:
            tkinterUtil.error("錯誤", "執行錯誤!" + err);
        return out
        

if __name__ == '__main__':
    MainUI()
