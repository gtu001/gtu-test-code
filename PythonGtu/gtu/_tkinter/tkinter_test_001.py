from tkinter import Label, Button, Radiobutton, Checkbutton, Entry, Frame, LabelFrame, Listbox, Text, Message, Scrollbar, \
    Scale, Spinbox, Menu, Menubutton, Canvas, Image, Toplevel, OptionMenu
from tkinter.ttk import Panedwindow

import tkinter as tk


win = tk.Tk()

win.title("test win")

# 設為0,0則為不可調整
# win.resizable(0,0)

label = Label(win, text="Hello World!")
label.pack()
button = Button(win, text="OK")
button.pack()
radio = Radiobutton(win, text="Radio")
radio.pack()
chkbtn = Checkbutton(win, text="ChkBtn")
chkbtn.pack()
entry = Entry(win, text="Entry")
entry.pack()
# frame = Frame(win, text="Frame")
# frame.pack()
labelFrame = LabelFrame(win, text="LabelFrame")
labelFrame.pack()
# lstbox = Listbox(win, text="Listbox")
# lstbox.pack()
# text = Text(win, text="Text")
# text.pack()
message = Message(win, text="Message")
message.pack()
# panedwindow = Panedwindow(win, text="Panedwindow")
# panedwindow.pack()
# scrollbar = Scrollbar(win, text="Scrollbar")
# scrollbar.pack()
# scale = Scale(win, text="Scale")
# scale.pack()
spinbox = Spinbox(win, text="Spinbox")
spinbox.pack()
# menu = Menu(win, text="Menu")
# menu.pack()
# optionmenu = OptionMenu(win, text="OptionMenu")
# optionmenu.pack()
menubutton = Menubutton(win, text="Menubutton")
menubutton.pack()
# canvas = Canvas(win, text="Canvas")
# canvas.pack()
# image = Image(win, text="Image")
# image.pack()
# bitmap = Bitmap(win, text="Bitmap")
# bitmap.pack()
# toplevel = Toplevel(win, text="Toplevel")
# toplevel.pack()


win.mainloop()
