

import numpy as np
import pandas as pd
from gtu.io import fileUtil
from gtu.reflect import checkSelf



def main(filePath):
    checkSelf.checkMembers(pd)
    pass


if __name__ == '__main__' :
    filePath = "E:/workstuff/workspace/gtu-test-code/PythonGtu/gtu/openpyxl_test/ex2/RCRP0S108.xlsx"
    main(filePath)