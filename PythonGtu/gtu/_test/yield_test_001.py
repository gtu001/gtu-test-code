def myrange(n):
    x = 0
    while True:
        yield x
        x += 1
        if x == n:
            break


for i in myrange(10):
    print(i, end='')
print()
