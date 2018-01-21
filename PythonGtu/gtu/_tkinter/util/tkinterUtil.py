from tkinter import filedialog, Text
from tkinter import messagebox


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
    
def warning(title, message):
    messagebox.showwarning(title, message)
    
        
if __name__ == '__main__':
    '''test'''
    message("title", "message")