from tkinter import filedialog, Text
from tkinter import messagebox

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
    messagebox.showerror(title, message)
    
    
def warning(title, message):
    messagebox.showwarning(title, message)
    
        
        
if __name__ == '__main__':
    '''test'''
    message("title", "message")