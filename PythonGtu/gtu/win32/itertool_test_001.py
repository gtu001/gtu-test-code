import itertools


yl = [5, 10, 15]
# for item in itertools.cycle(yl):
#     print(item)

v2 = itertools.cycle(yl)

print(v2.__next__())