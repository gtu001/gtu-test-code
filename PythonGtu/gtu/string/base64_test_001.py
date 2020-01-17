import base64

'''
from gtu.string import base64_test_001
'''

def encode(s, encoding="utf8") :
    return base64.b64encode(s.encode(encoding))

def decode(a):
    return base64.b64decode(a)




if __name__ == '__main__' :
    print("decode --------------")

    strValArry = [
        'Y2FzaHVzZXI=',
        'Y2FzaHVzZXI=',
        "amRiYzpqdGRzOnNxbHNlcnZlcjovLzEyNy4wLjAuMToxNDMzL0NBU0hfVVVBVA=="
    ]

    for i, strVal in enumerate(strValArry) :
        result = decode(strVal)
        print(i, result)

    print("encode --------------")

    strValArry2 = [
        'sa',
        '1qaz@WSX#',
        'jdbc:sqlserver://10.1.117.144;databaseName=CASH_UUAT'
    ]

    for i, strVal in enumerate(strValArry2) :
        result = encode(strVal)
        print(i, result)

    print("done..")
    