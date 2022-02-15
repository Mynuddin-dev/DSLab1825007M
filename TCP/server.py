import socket
import threading

HOST = "192.168.161.178"
PORT = 7777

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((HOST, PORT))

server.listen()


def recieve():
  client, address = server.accept()

  name = client.recv(1024).decode('ascii')


  print(f'Connected with {address}')
  client.send(f'Echo from the server {name}'.encode('ascii'))
  client.close()


print('Server is listening')
recieve()