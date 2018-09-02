import socket
HOST = '127.0.0.1'
PORT = 65535

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))

while True:
    cmd = input("Please input msg:")
    print("cmd : ", cmd)
    s.send(cmd.encode("utf8"))
    data = s.recv(1024)
    print(data)

    s.close()