from functools import wraps
from random import randint
from time import time, sleep


def record(output):
    def decorate(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            start = time()
            ret_value = func(*args, **kwargs)
            output(func.__name__, time() - start)
            return ret_value
        return wrapper
    return decorate


def output_to_console(fname, duration):
    print('%s: %.3f秒' % (fname, duration))


def output_to_file(fname, duration):
    with open('log.txt', 'a') as file_stream:
        file_stream.write('%s: %.3f秒\n' % (fname, duration))


@record(output_to_console)
def random_delay(min, max):
    sleep(randint(min, max))


def main():
    for _ in range(3):
        # print(random_delay.__name__)
        random_delay(3, 5)
    # for _ in range(3):
    #     # 取消掉装饰器
    #     random_delay.__wrapped__(3, 5)


if __name__ == '__main__':
    main()
