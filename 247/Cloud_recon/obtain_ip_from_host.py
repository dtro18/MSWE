import socket

def get_ip_address(subdomain):
  try:
    ip_address = socket.gethostbyname(subdomain)
    return ip_address
  except socket.gaierror:
    return None
subdomain_list = []

with open('subdomains.txt', "r") as file:
    for line in file.readlines():
       subdomain_list.append(line.rstrip('\n'))

with open('ip_addresses.txt', "w") as file:
    for item in subdomain_list:
       item += ".uci.edu"
       ip = get_ip_address(item)
       if ip:
          print(ip)
          file.write(ip + "\n")
