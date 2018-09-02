


class _SwitchDefObj():
    def aa(self, *arg, **arg2):
        v1, v2, v3, v4 = self.paramHander(arg, arg2)
        print("aa", v1, v2, v3, v4)
    def bb(self, *arg, **arg2):
        v1, v2, v3, v4 = self.paramHander(arg, arg2)
        print("bb", v1, v2, v3, v4)
    def cc(self, *arg, **arg2):
        v1, v2, v3, v4 = self.paramHander(arg, arg2)
        print("cc", v1, v2, v3, v4)
    def default(self, *arg, **arg2):
        v1, v2, v3, v4 = self.paramHander(arg, arg2)
        print("default", v1, v2, v3, v4)
    def paramHander(self, arg, arg2):
        v1 = arg[0]
        v2 = arg[1]
        v3 = arg[2]
        v4 = arg2['ok']
        return v1,v2,v3,v4
        

def getSwitchFun(val, clzInstance, switchFunMap, defaultMethodName):
    methodName = switchFunMap.get(val, defaultMethodName)
    method = None
    try:
        method = getattr(clzInstance, methodName)
    except AttributeError:
        raise NotImplementedError("Class `{}` does not implement `{}`".format(clzInstance.__class__.__name__, methodName))
    return method
    

if __name__ == '__main__' :
    switchFunMap = {
        1:"aa",
        2:"bb",
        3:"cc",
        }
    
    _inst = _SwitchDefObj()
    
    getSwitchFun(1, _inst, switchFunMap, "default")(1,2,3, ok=True)
    getSwitchFun(2, _inst, switchFunMap, "default")(1,2,3, ok=True)
    getSwitchFun(3, _inst, switchFunMap, "default")(1,2,3, ok=True)
    getSwitchFun(4, _inst, switchFunMap, "default")(1,2,3, ok=True)

    print("done...")