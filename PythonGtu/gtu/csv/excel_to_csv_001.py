import csv
from pathlib import Path
import openpyxl
from gtu.io import fileUtil
from gtu.openpyxl_test import excelUtil



def convertToCsv(excelPath, targetCsvFile):
    wb1 = openpyxl.load_workbook(excelPath, read_only=True, data_only=True)
    sheet = excelUtil.getSheetByIndex(wb1, 0)
    
    csvfile = open(targetCsvFile, 'w', newline='', encoding='utf-8')
    spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        
    for rp, row in enumerate(sheet, 0):
        rowLst = list()
        for cp, cell in enumerate(row, 0):
            val = ""
            if cell.value is not None :
                val = str(cell.value).strip()
            rowLst.append(val)
        spamwriter.writerow(rowLst)



if __name__ == '__main__' :
    filePath = "C:/Users/gtu00/OneDrive/Desktop/jp_50_character.xlsx"
    targetFile = fileUtil.getDesktopDir() + "ttttt.csv"
    convertToCsv(filePath, targetFile)
    print("done...")
    
    
    
    
    
