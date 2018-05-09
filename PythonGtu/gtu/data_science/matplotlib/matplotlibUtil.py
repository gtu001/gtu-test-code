

import matplotlib.pyplot as plt
import seaborn; 
import numpy as np
from gtu.reflect import checkSelf

'''
from gtu.data_science.matplotlib import matplotlibUtil
'''

def hist(npArry, title=None, xlabel=None, ylabel=None, seabornStyle=False):
    if seabornStyle:
        seaborn.set()  # set plot style
        
    plt.hist(npArry)
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel);
    plt.show()
    

def show_twoDim(npArry, seabornStyle=False):
    if seabornStyle:
        seaborn.set()  # set plot style
        
    plt.imshow(npArry, origin='lower', cmap='viridis')
    plt.colorbar()
    plt.show()


if __name__ == '__main__' :
    npArry = np.random.randint(1, high=10, size=20)
    hist(npArry)
    print("done...")
