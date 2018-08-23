import pdftotext


file = 'C:/Users/wistronits/Desktop/台達/ISA-95/ISA-95.00.02-2010.pdf'

pdf = pdftotext.PDF(file)

# Iterate over all the pages
for page in pdf:
    print(page)