    
    
def addMap(k, val, map2):
    v = []
    if map2.__contains__(k):
        v = map2[k]
    v.append(val)
    map2[k] = v
    
