import json, socket

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
    host = "26.202.89.163"
    port = 12345

    try:
        Clientsocket.connect((host, port))
        return 3
    except socket.error as e:
        return 2
        pass

    data = transmitData('createaddress', [entropy, id])
    Clientsocket.sendall(data.encode())

    return 1


# import numpy as np
# def foo():
#     # rs = np.zeros(5)
#     rs = np.zeros(5)
#     return "hello" + str(rs)