import base64
import json
import redis



class Person(object):
    """人"""
    def __init__(self, name, age):
        self.name = name
        self.age = age
    # def __gt__(self, other):
    #     return self.name > other.name
    def __str__(self):
        return f'{self.name}: {self.age}'
    def __repr__(self):
        return self.__str__()


class PersonJsonEncoder(json.JSONEncoder):

    def default(self, o):
        return o.__dict__


def main():
    cli = redis.StrictRedis(host='120.77.222.217', port=6379, 
                            password='123123')
    data = base64.b64decode(cli.get('guido'))
    with open('guido2.jpg', 'wb') as file_stream:
        file_stream.write(data)
    # with open('guido.jpg', 'rb') as file_stream:
    #     result = base64.b64encode(file_stream.read())
    # cli.set('guido', result)
    # persons = [
    #     Person('骆昊', 39), Person('王大锤', 18),
    #     Person('白元芳', 25), Person('狄仁杰', 37)
    # ]
    # persons = json.loads(cli.get('persons'))
    # print(persons)
    # cli.set('persons', json.dumps(persons, cls=PersonJsonEncoder))


if __name__ == '__main__' :
    main()