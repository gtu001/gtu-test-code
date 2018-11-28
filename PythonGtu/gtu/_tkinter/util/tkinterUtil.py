from tkinter import filedialog, Text
from tkinter import messagebox
from tkinter import simpledialog;

'''
from gtu._tkinter.util import tkinterUtil
'''
from gtu.reflect import checkSelf
from gtu.error import errorHandler
from gtu.io import fileUtil
import os


def _defaultArgs(args, isDir=True, exists=None):
    if 'title' not in args  :
        args['title'] = "開啟檔案"
    if 'filetypes' not in args or \
        ('filetypes' in args or args['filetypes'] is None ) :
        args['filetypes'] = [("all", "*.*")]
    if 'initialdir' not in args or \
        ('initialdir' in args and args['initialdir'] is None ) or \
        ('initialdir' in args and not os.path.isdir(args['initialdir'])) :
        args['initialdir'] = fileUtil.getDesktopDir()
    if 'initialfile' not in args or \
        ('initialfile' in args and args['initialfile'] is None ) or \
        ('initialfile' in args and not os.path.exists(args['initialfile'])) :
        args['initialfile'] = fileUtil.getDesktopDir()
    if isDir :
        if 'filetypes' in args : del args['filetypes']
        if 'initialfile' in args : del args['initialfile']
    args['mustexist'] = exists
    print(args)
    return args

def askopenfilename(**args):
    args = _defaultArgs(args, isDir=False)
    return filedialog.askopenfilename(**args)


def askdirectory(**args):
    args = _defaultArgs(args, isDir=True)
    return filedialog.askdirectory(**args)


def confirm(title, message):
    result = messagebox.askquestion(title, message, icon='warning')
    if result == 'yes':
        return True
    else:
        return False


def message(title, message):
    messagebox.showinfo(title, message)
    
    
def error(title, message):
    messagebox.showerror(title, message)
    

def error_ex(title, exception):
    # 自訂檢核錯誤     
    if type(exception) == ValidateException :
        messagebox.showerror(title=exception.title, message=exception.args[0])
    # 意外錯誤
    else :
        message = errorHandler.getStackTraceToStr(exception)
        print(message)
        messagebox.showerror(title, message)
    
    
def warning(title, message):
    messagebox.showwarning(title, message)
    
    
def prompt(title, message, defaultVal=None):
#     answer = simpledialog.askstring(title, message)
    answer = simpledialog.askstring(title=title, prompt=message, initialvalue=defaultVal)
    return answer

    
def centerWin(win):
    """
    centers a tkinter window
    :param win: the root or Toplevel window to center
    """
    win.update_idletasks()
    width = win.winfo_width()
    frm_width = win.winfo_rootx() - win.winfo_x()
    win_width = width + 2 * frm_width
    height = win.winfo_height()
    titlebar_height = win.winfo_rooty() - win.winfo_y()
    win_height = height + titlebar_height + frm_width
    x = win.winfo_screenwidth() // 2 - win_width // 2
    y = win.winfo_screenheight() // 2 - win_height // 2
    win.geometry('{}x{}+{}+{}'.format(width, height, x, y))
    win.deiconify()
    
    
def createDefaultWin():
    import tkinter as tk
    root = tk.Tk();
    root.withdraw()
    
    
class ValidateException(Exception):
    def __init__(self, message, title=None):
        if title is None :
            self.title = "錯誤"
        else : 
            self.title = title 
        self.args = (message, self.title)
        
        
        
if __name__ == '__main__':
    '''test'''
#     message("title", "message")
    askdirectory(initialdir = '/home/gtu001/桌面', initialfile = '/home/gtu001/桌面')
