import java.net.*;
import java.io.*;
import java.util.HashMap;
public class Client {
    public final static int PORT = 8080;
    public final static int MAX_PACKET_SIZE = 1024 + 4; // Data + Sequence Number

    public static void main(String[] args) {
        String hostname = "localhost";
        try (DatagramSocket theSocket = new DatagramSocket()) {
            InetAddress server = InetAddress.getByName(hostname);
            // Grab user input

            if (args[0].equals("index")) {
                System.out.println("Printing available files: \n");
                sendRequest("INDEX", theSocket, server);
                receiveIndexResponse(theSocket);

            // Get request and filename specified
            } else if (args.length == 2 && args[0].equals("get")) {
                System.out.println("Attempting to retrieve: " + args[1]);
                sendRequest("GET " + args[1], theSocket, server);
                receiveGetReponse(theSocket);
            } else {
                System.out.println("Error in CL arguments. \n");
            }
            
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    private static void sendRequest(String request, DatagramSocket socket, InetAddress server) {
        byte[] data = request.getBytes();
        DatagramPacket theOutput = new DatagramPacket(data, data.length, server, PORT);
        try {
            socket.send(theOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void receiveIndexResponse(DatagramSocket socket) {
        System.out.println("System entered INDEX");
        byte[] buffer = new byte[MAX_PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
            String content = new String(packet.getData(), 0, packet.getLength());
            System.out.println(content);
        }
         catch (IOException e) {
            // idk
        }
    }

    private static void receiveGetReponse(DatagramSocket socket) {
        System.out.println("System entered GET");

        byte[] buffer = new byte[MAX_PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        int totalPackets = 0;
        int receivedPackets = 0;
        HashMap<Integer, String> contentHashMap = new HashMap<Integer, String>();

        while (true) {
            // Receive a packet
            try {
                socket.receive(packet);
                // Extract the sequence number
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                DataInputStream dis = new DataInputStream(bais);

                // Read the sequence number (first 4 bytes)
                int sequenceNumber = dis.readInt();
                System.out.println("Sequence number: " + sequenceNumber);
                
                if (sequenceNumber == -1) {
                    totalPackets += dis.readInt();
                } else if (sequenceNumber == -2) {
                    System.out.println("Error reading file.");
                    break;
                }
                // Read the data (remaining bytes)
                byte[] data = new byte[packet.getLength() - 4];
                dis.readFully(data);

                // Convert the data to a string for demonstration
                String content = new String(data);
                System.out.println("Content: " + content);
                // Check the content to make sure no errors
                
                contentHashMap.put(sequenceNumber, content);

                // Print the sequence number and content
                receivedPackets++;
                System.out.println("Received packet #" + sequenceNumber);
            }
             catch (IOException e) {

            }
            if (totalPackets > 0 && receivedPackets == totalPackets) {
                System.out.println("All packets received correctly");
                // Uncomment this to see the reconstructed text
                // printHashMapValues(contentHashMap);
                break;
            }
            
        }
            
    }
    private static void printHashMapValues(HashMap<Integer, String> hashMap) {
        for (String value : hashMap.values()) {
            System.out.println(value);
        }
    }
}