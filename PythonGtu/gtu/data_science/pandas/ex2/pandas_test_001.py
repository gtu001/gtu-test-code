import sys
import pandas as pd
from gtu.io import fileUtil
from gtu.reflect import checkSelf
import matplotlib.pyplot as plt
import numpy as np
from gtu.data_science.matplotlib import matplotlibUtil

def main():
    input_file = fileUtil.getCurrentDir() + "pandas_ex2.csv"
    data_frame = pd.read_csv(input_file)
    print(data_frame)
    
    print("-----------------------------------------------------------")
    data_frame['cost'] = data_frame['cost'].astype(float)  # .str.strip()
    loc_data = (data_frame['supplier name'].str.contains('Z')) | (data_frame['cost'] > 600.0)
    data_frame_value_meets_condition = data_frame.loc[loc_data, :]
    data_frame_value_meets_condition.to_csv(getOuput_file("data_frame_value_meets_condition"), index=False)
    print(data_frame_value_meets_condition)
    print("-----------------------------------------------------------")
    
    important_dates = ['1/20/2014', '1/30/2014']
    data_frame_value_in_set = data_frame.loc[data_frame['purchase date'].\
                                             isin(important_dates), :]
    data_frame_value_in_set.to_csv(getOuput_file("data_frame_value_in_set"), index=False)
    print(data_frame_value_in_set)
    print("-----------------------------------------------------------")
    
    


def getOuput_file(filename):
    output_file = fileUtil.getDesktopDir() + filename + ".csv"
    return output_file


if __name__ == '__main__' :
    main()
