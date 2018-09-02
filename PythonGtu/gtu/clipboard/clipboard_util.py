import win32clipboard


def getClipboardData():
    '''取得剪貼簿'''
    win32clipboard.OpenClipboard()
    clipboarditem = win32clipboard.GetClipboardData()
    print("contents "+clipboarditem)
    win32clipboard.CloseClipboard()
    return clipboarditem
    