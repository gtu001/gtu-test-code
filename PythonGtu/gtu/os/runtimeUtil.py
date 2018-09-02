
import subprocess 
import os

'''
from gtu.os import runtimeUtil
'''


def runtimeExec(command):
    subprocess.check_output(command, shell=True)
    
    
def runtimeExec2(command):
    os.system(command)
    
    
def runtimeExec3(command, decode):
    process = subprocess.Popen(command, shell=True,
                           stdout=subprocess.PIPE, 
                           stderr=subprocess.PIPE)

    # wait for the process to terminate
    out, err = process.communicate()
    errcode = process.returncode
    out = str(out, decode)
    err = str(err, decode)
    return (out, err, errcode)