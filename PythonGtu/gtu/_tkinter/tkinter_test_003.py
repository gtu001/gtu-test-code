import tkinter as tk


class Example(tk.Frame):
    def __init__(self, parent):
        tk.Frame.__init__(self, parent)
        self.text = tk.Text(self, wrap="none")
        self.text.pack(fill="both", expand=True)

        self.text.bind("<ButtonRelease-1>", self._on_click)
        self.text.tag_configure("highlight", background="green", foreground="black")

        with open(__file__, "rU") as f:
            data = f.read()
            self.text.insert("1.0", data)

    def _on_click(self, event):
        self.text.tag_remove("highlight", "1.0", "end")
        self.text.tag_add("highlight", "insert wordstart", "insert wordend")

if __name__ == "__main__":
    root = tk.Tk()
    Example(root).pack(fill="both", expand=True)
    root.mainloop()