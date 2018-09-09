import time


def generatorFunction(max_val):
    for i in range(0, 5):
        time.sleep(1)
        yield "String %d" % i


def smallGenerator():
    for item in generatorFunction(3):
        yield item


for s in smallGenerator():
    print(s)
