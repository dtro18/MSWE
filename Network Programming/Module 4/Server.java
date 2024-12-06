import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Server {
    public final static int PORT = 8080;
    public final static int MAX_PACKET_SIZE = 65507;
    public static Path directory = null;
    // For testing - main dir
    // C:\\MSWE Projects\\Network Programming\\Module 3\\textFilesDir
    // C:\\MSWE Projects\\Network Programming\\Module 2
    public static void main(String[] args) {
        // Find file directory
        try {
            System.out.println(args[0]);
            directory = Paths.get(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create a buffer to hold bytes.
        byte[] buffer = new byte[MAX_PACKET_SIZE];
        // Create a socket on the specified port.
        try (DatagramSocket server = new DatagramSocket(PORT)) {
            while (true) {
                try {
                    // Create a packet to hold the incoming request
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    server.receive(packet);
                    InetAddress address = packet.getAddress();
                    int clientPort = packet.getPort();
                    // Receives data from packet, decodes it, then stores it as a variable.
                    String request = new String(packet.getData(), 0, packet.getLength());
                    System.out.println(packet.getAddress() + " at port " + packet.getPort() + " says " + request);

                    // Control flow for client requests
                    if (request.startsWith("GET ")) {
                        String fileString = request.substring(4).trim();
                        // Construct the file's path and pass to be chunked
                        Path filePath = directory.resolve(fileString);
                        chunkFile(server, filePath, address, clientPort);
                    } else if (request.trim().equals("INDEX")){
                        sendIndex(server, directory, address, clientPort);
                    } else {
                        System.out.println("Unknown request: " + request);
                    }
                    
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            } // end while
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    private static void chunkFile(DatagramSocket server, Path filePath, InetAddress address, int clientPort) {
        int sequenceNumber = 0;
        byte[] buffer = new byte [1024];
        // Test path
        // C:\\MSWE Projects\\Network Programming\\Module 2\\text1.txt

        // Send control packet which has a sequence number of -1
        try {
            long fileSize = Files.size(filePath);
            int totalPackets = (int) Math.ceil((double) fileSize / buffer.length);
            // Send total packet count as a control packet
            ByteArrayOutputStream controlStream = new ByteArrayOutputStream();
            DataOutputStream dosControl = new DataOutputStream(controlStream);
            dosControl.writeInt(-1); // Use -1 as an identifier for the control packet
            dosControl.writeInt(totalPackets); // Include total packet count
            byte[] controlData = controlStream.toByteArray();
    
            DatagramPacket controlPacket = new DatagramPacket(controlData, controlData.length, address, clientPort);
            server.send(controlPacket);
            System.out.println("Sent control packet with total packet count: " + totalPackets);
        
            
            try (FileInputStream fis = new FileInputStream(filePath.toString())) {
                int bytesRead;
                // Loop through to get the actual file data
                while ((bytesRead = fis.read(buffer)) != -1) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);

                    // Write sequence number to the stream
                    dos.writeInt(sequenceNumber);
                    // Write the actual data chunk to the stream
                    dos.write(buffer, 0, bytesRead);

                    byte[] packetData = baos.toByteArray();
                    DatagramPacket packet = new DatagramPacket(packetData, packetData.length, address, clientPort);
                    server.send(packet);

                    // Printing out chunks:
                    System.out.println("Sent packet #" + sequenceNumber);
                    System.out.println("Chunk data (sequence " + sequenceNumber + "):");
                    // System.out.println(new String(buffer, 0, bytesRead)); // Debugging as a string (if text)
        
                    // Increment sequence number for the next chunk
                    sequenceNumber++;
                }
                System.out.println(totalPackets);
            } catch (IOException e) {
                System.err.println("Error reading the file: " + e.getMessage());
            }
            // Code to tell client to check its shit
            ByteArrayOutputStream endStreamBAOS = new ByteArrayOutputStream();
            DataOutputStream endStreamDOS = new DataOutputStream(endStreamBAOS);
            endStreamDOS.writeInt(-3); // Use -1 as an identifier for the control packet
            endStreamDOS.writeInt(totalPackets);
            byte[] endStreamData = endStreamBAOS.toByteArray();
    
            DatagramPacket endPacket = new DatagramPacket(endStreamData, endStreamData.length, address, clientPort);
            server.send(endPacket);
            System.out.println("Sent end packet with total packet count: " + totalPackets);

        } catch (IOException e) {
           
            byte[] data = ("Error reading file.").getBytes();
            try {
                ByteArrayOutputStream errorStreamBAOS = new ByteArrayOutputStream();
                DataOutputStream errorStreamDOS = new DataOutputStream(errorStreamBAOS);
                errorStreamDOS.writeInt(-2); // Use -1 as an identifier for the control packet
                errorStreamDOS.write(data); // Include total packet count
                byte[] errorData = errorStreamBAOS.toByteArray();
        
                DatagramPacket errorPacket = new DatagramPacket(errorData, errorData.length, address, clientPort);
                server.send(errorPacket);
                
            } catch (IOException exception) {
                System.err.println("Error reading the file: " + e.getMessage());
            }
        }
    }
    // Designed to send only one packet including all the text files.
    private static void sendIndex (DatagramSocket server, Path filePath, InetAddress address, int clientPort) {
        
        StringBuilder sb = new StringBuilder();
        try (Stream<Path> paths = Files.list(directory)){
            paths.filter(p -> p.getFileName().toString().endsWith(".txt")).forEach(p -> {   
                if (Files.exists(p) && Files.isReadable(p)) {
                    sb.append(p.getFileName().toString() + "\n");
                } else {
                    System.err.println("File is either missing or unreadable: " + p);
                }
            });
    
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        byte[] data = sb.toString().getBytes();
        try {
            DatagramPacket theOutput = new DatagramPacket(data, data.length, address, clientPort);
            server.send(theOutput);
        } catch (IOException e) {

        }
    }
}