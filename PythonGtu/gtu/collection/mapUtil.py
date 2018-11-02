

    
    
def appendList(k, val, map):
    v = []
    if map.__contains__(k):
        v = map[k]
    v.append(val)
    map[k] = v
    


def get(map, key, defaultVal=None):
    return map.get(key, defaultVal)


def getSortedKeys(map):
    return sorted(map, key=map.get)


def getSortedValues(map):
    if False :
        from operator import itemgetter
        return sorted(map.items(), key=itemgetter(1))
    elif True :
        return sorted(map.items(), key=lambda x : x[1])

    

if __name__ == '__main__' :
    lst = [1,2,3,4,5,6]
    
    print(','.join(map(str, lst)))
    
    
    print("done...")
    
    