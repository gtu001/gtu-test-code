from tkinter import Text, Button, Label

import tkinter as tk

class MainUI():
    def __init__(self):
        win = tk.Tk()
    
        win.title("產生36報表excel")
        l1 = Label(win, text="路徑:")
        l1.grid(column=0, row=0)
        
        self.text = Text(win, width=30, height=2, font=("Courier", 10))
        self.text.bind("<ButtonRelease-1>", self.callback)
        self.text.bind("<Button-1>", self.callback)
        self.text.bind("<Enter>", self.callback)
        self.text.bind("<Leave>", self.callback)
        self.text.grid(column=1, row=0)
        
        self.btn = Button(win, text="執行", command=self.btnClick)
        self.btn.grid(column=2, row=0)
        
        self.label = Label(win, text="結果!")
        self.label.grid(column=0, row=1)
        
        win.mainloop()

    def btnClick(self):
        print("btnClick")
        self.label.config(text="testOk")
    
    def callback(self, event):
        str = self.text.get(1.0, "end")
        print(str)

if __name__ == '__main__':
    MainUI()
