
from tkinter.ttk import Combobox

import tkinter as tk


class Example(tk.Frame):
    def __init__(self, parent):
        tk.Frame.__init__(self, parent)
        
        self.combobox = Combobox(self, values=["a", "b"])
        self.combobox.bind('<<ComboboxSelected>>', self.comboboxClick)
        self.combobox.grid(row=0, column=0)
        self.combobox.current(1)  # set as default "option 2"

    def comboboxClick(self, e):
        print(self.combobox.get())
        self.combobox.configure(values=["cvv"])
        
if __name__ == "__main__":
    root = tk.Tk()
    Example(root).pack(fill="both", expand=True)
    root.mainloop()
