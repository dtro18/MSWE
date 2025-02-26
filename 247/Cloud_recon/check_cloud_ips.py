import ipaddress
import requests
import json

# Load UCI IP addresses from file
with open('ip_addresses.txt', 'r') as f:
    uci_ips = [line.strip() for line in f.readlines()]

# Function to fetch cloud provider IPs
def get_cloud_ips(url, key):
    response = requests.get(url)
    data = response.json()
    return [ip['ip_prefix'] for ip in data[key] if 'ip_prefix' in ip]

# Fetch cloud provider IP ranges
aws_ips = get_cloud_ips("https://ip-ranges.amazonaws.com/ip-ranges.json", "prefixes")
gcp_ips = get_cloud_ips("https://www.gstatic.com/ipranges/cloud.json", "prefixes")
azure_ips = []  # Azure IPs need to be parsed manually from XML or JSON
print(aws_ips[0])

# Convert to IP networks to compare ranges given by cloud providers
aws_networks = [ipaddress.ip_network(ip) for ip in aws_ips]
gcp_networks = [ipaddress.ip_network(ip) for ip in gcp_ips]

# Function to check if an IP belongs to any cloud provider
def check_cloud_provider(ip):
    ip_addr = ipaddress.ip_address(ip)
    
    if any(ip_addr in network for network in aws_networks):
        return "AWS"
    if any(ip_addr in network for network in gcp_networks):
        return "Google Cloud"
    return "Unknown"

# Cross-check each UCI IP
results = {}
for ip in uci_ips:
    provider = check_cloud_provider(ip)
    results[ip] = provider

# Print and save results
with open("aws_ip_addresses.txt", "w") as f:
    for ip, provider in results.items():
        print(f"{ip} -> {provider}")
        if provider == "AWS":
            f.write(ip + "\n")

with open("gc_ip_addresses.txt", "w") as f:
    for ip, provider in results.items():
        print(f"{ip} -> {provider}")
        if provider == "Google Cloud":
            f.write(ip + "\n")
