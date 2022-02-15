import socket
import threading

nickname = ""

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(("192.168.161.178", 7777))


def recieve():
  try:
    message = client.recv(1024).decode('ascii')
    if message == 'Enter your Nickname:':
      client.send(nickname.encode('ascii'))
    else:
      print(message)
  except:
    print('Error occured')
    
    


def write():
  message = f'{nickname}: {input("Message : ")}'
  client.send(message.encode('ascii'))



recieve_thread = threading.Thread(target=recieve)
recieve_thread.start()

write_thread = threading.Thread(target=write)
write_thread.start()

client.close()
