from tkinter import filedialog, Text
from tkinter import messagebox
from tkinter import simpledialog;

'''
from gtu._tkinter.util import tkinterUtil
'''
from gtu.reflect import checkSelf
from gtu.error import errorHandler


def askopenfilename():
    return filedialog.askopenfilename()


def askdirectory():
    return filedialog.askdirectory()


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
    message = errorHandler.getStackTraceToStr(exception)
    print(message)
    messagebox.showerror(title, message)
    
    
def warning(title, message):
    messagebox.showwarning(title, message)
    
    
def prompt(title, message, defaultVal=None):
#     answer = simpledialog.askstring(title, message)
    answer = simpledialog.askstring(title = title, prompt = message, initialvalue=defaultVal)
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
        
        
if __name__ == '__main__':
    '''test'''
    message("title", "message")
