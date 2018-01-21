

def upper(lst):
    return [k.upper() for k in lst]


def _test_filter():
    lst = ['a', 'b', 'a1', 'd', 'a3']
    lst2 = filter(lambda x : x.startswith("a"),  lst)
    for i in lst2:
        print("result", i)
        
    lst3 = [x for x in lst if x.startswith("a")]
    print(lst3)
    
    
if __name__ == '__main__' :
    _test_filter()