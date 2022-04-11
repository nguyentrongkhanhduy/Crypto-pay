import json
import socket

class transmitData:
    def __init__(self, task: str, param: list):
        self.task = task
        self.param = param

    def toJSON(self):
        return {
            'task': self.task,
            'param': self.param
        }

    def encode(self):
        return json.dumps(self.toJSON()).encode()

def createUser(id, entropy):
    Clientsocket = socket.socket()
    host = "192.168.11.130"
    port = 12345
    try:
        Clientsocket.connect((host, port))
    except socket.error as e:
        pass
    data = transmitData('createaddress', [entropy, id])
    Clientsocket.sendall(data.encode())
    Clientsocket.close()

    return 1

def widthdraw(amount , address):
    Clientsocket = socket.socket()
    host = "192.168.11.130"
    port = 12345
    try:
        Clientsocket.connect((host, port))
    except socket.error as e:
        pass
    data = transmitData('widthdrawn', [amount, address])
    Clientsocket.sendall(data.encode())
    Clientsocket.close()

    return 1

