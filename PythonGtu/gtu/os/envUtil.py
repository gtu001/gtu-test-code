import os

# ## from gtu.os import envUtil

def export(key, val):
    os.environ[key] = val
    
    
def getEnv(key, default=""):
    return os.getenv(key, default)


def showAll():
    for k, v in os.environ.items() :
        print("env\t", k, "\t" , v)
    
    
if __name__ == '__main__' :
    showAll()
    