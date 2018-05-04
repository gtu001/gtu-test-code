import random

def __test_upper(lst):
    return [k.upper() for k in lst]



def __test_filter():
    lst = ['a', 'b', 'a1', 'd', 'a3']
    lst2 = filter(lambda x : x.startswith("a"),  lst)
    for i in lst2:
        print("result", i)
        
    lst3 = [x for x in lst if x.startswith("a")]
    print(lst3)
    
    
def __test_filter_useFunc():
    lst = list()
    for i in range(0, 10):
        lst.append(random.randint(0, 100))
        
    def check(row):
        return row > 50
        
    lst = list(filter(check, lst))
    print(lst)
    
    
if __name__ == '__main__' :
#     __test_filter()
    __test_filter_useFunc()