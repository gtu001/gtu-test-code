import sys
import traceback 

'''
from gtu.thread import threadUtil
'''    

    
def printCurrentStacks():
    print("*** STACKTRACE - START ***")
    code = []
    for threadId, stack in sys._current_frames().items():
        code.append("# ThreadID: %s" % threadId)
        for filename, lineno, name, line in traceback.extract_stack(stack):
            code.append('File: "%s", line %d, in %s' % (filename,
                                                        lineno, name))
            if line:
                code.append("  %s" % (line.strip()))
    
    for line in code:
        print(line)
    print("*** STACKTRACE - END ***")
    
    
def getCurrentRunning(ignorePy = []):
    for threadId, stack in sys._current_frames().items():
        for filename, lineno, name, line in traceback.extract_stack(stack):
            if filename in ignorePy :
                continue;
            return (threadId, filename, lineno, name, line)
    
    
if __name__ == '__main__':
    printCurrentStacks()
    print("done..")
