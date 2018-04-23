import pdftotext


file = 'E:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/pdf/ex1/RCRP0S102.pdf'

pdf = pdftotext.PDF(file)

# Iterate over all the pages
for page in pdf:
    print(page)