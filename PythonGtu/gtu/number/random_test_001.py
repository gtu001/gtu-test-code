import random
from gtu.reflect import checkSelf


'''隨機整數：'''
v = random.randint(0,99)
print("1 = ", v)

'''隨機選取0到100間的偶數：'''
v = random.randrange(0, 101, 2)
print("2 = ", v)

'''隨機浮點數：'''
v = random.random()
print("3 = ", v)
v = random.uniform(1, 10)
print("4 = ", v)

'''隨機字元：'''
v = random.choice('abcdefg&#%^*f')
print("5 = ", v)

'''多個字元中選取特定數量的字元：'''
v = random.sample('abcdefghij',3) 
print("6 = ", v)

'''多個字元中選取特定數量的字元組成新字串：'''
v = random.sample(['a','b','c','d','e','f','g','h','i','j'], 3)
v = "".join(v)
print("7 = ", v)

'''隨機選取字串：'''
v = random.choice ( ['apple', 'pear', 'peach', 'orange', 'lemon'] )
print("8 = ", v)

'''洗牌：'''
items = [1, 2, 3, 4, 5, 6]
random.shuffle(items)
print("9 = ", items)
