import socket

sock = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)      # For UDP

udp_host = socket.gethostname()			# Host IP
udp_port = 1111          		        # specified port to connect

msg = "Hello Python!"
print ("UDP target IP:", udp_host)
print ("UDP target Port:", udp_port)

sock.sendto(msg.encode('utf-8'),(udp_host,udp_port))

sock.close()