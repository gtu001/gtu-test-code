
import subprocess 
import os

### from gtu.os import runtimeUtil


def runtimeExec(command):
    subprocess.check_output(command, shell=True)
    
    
def runtimeExec2(command):
    os.system(command)