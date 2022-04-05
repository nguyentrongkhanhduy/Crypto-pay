import json, socket

Clientsocket = socket.socket()
host = "26.202.89.163"
port = 12345

try:
    Clientsocket.connect((host, port))
except socket.error as e:

    pass
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
    data = transmitData('createaddress', [entropy, id])
    Clientsocket.sendall(data.encode())

    return 1

def widthdraw(amount , address):
    data = transmitData('widthdrawn', [amount, address])
    Clientsocket.sendall(data.encode())

    return 1

