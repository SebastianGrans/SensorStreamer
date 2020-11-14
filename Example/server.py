import socket
import numpy as np
import struct # Unpacking bytes

HOST = '192.168.0.100' # The IP of the computer you are running this script on.
PORT = 8000 # A port of your choosing. 

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    conn, addr = s.accept()
    with conn:
        print("{} connected.".format(addr))
        while True:
            data = conn.recv(1024)

            if not struct.unpack('i', data[:4])[0] == 0 or \
                not data[4] == 128 or \
                not data[-3] == 0: 
                print(data)
                continue
            
            # big endian long long
            t = struct.unpack('>q', data[5:13])
            # floats
            rx = struct.unpack('>f', data[13:17])
            ry = struct.unpack('>f', data[17:21])
            rz = struct.unpack('>f', data[21:25])
            euler = np.array([rx[0], ry[0], rz[0]])
            print(euler)
