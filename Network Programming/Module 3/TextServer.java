import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;
import java.util.stream.Stream;


public class TextServer {
    public final static int PORT = 8080;
    public static Path directory = null;
    // For testing - main dir
    // C:\\MSWE Projects\\Network Programming\\Module 3\\textFilesDir
    // C:\\MSWE Projects\\Network Programming\\Module 2
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(50);
        // Serversocket listens for client connections on specified port
        try {
            System.out.println(args[0]);
            directory = Paths.get(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try {
                    // Accept client connection. Block until client connects. 
                    Socket connection = server.accept();
                    System.out.println("Connection established");
                    // Wrap connection in Daytime Task (callable implementation)
                    Callable<Void> task = new TextTask(connection);
                    // Submits task to thread pool
                    pool.submit(task);
                } catch (IOException ex) {}
            }
        } catch (IOException ex) {
            System.err.println("Couldn't start server");
        }
    }
    // Each daytime task represents one connection
    private static class TextTask implements Callable<Void> {
        // Create that connection
        private Socket connection;
        TextTask(Socket connection) {
            this.connection = connection;
        }
        @Override
        public Void call() {
            try {
                // Input stream writer handles code going to client
                Writer out = new OutputStreamWriter(connection.getOutputStream());
                String requestType = collectInput(connection);
                if (requestType.equals("INDEX")) {
                    out.write(serveIndex() + "\r\n");
                    out.flush();
                }
                else if (requestType.startsWith("GET")) {
                    String[] parts = requestType.split(" ", 2); // Split into "GET" and "<filename>"
                    if (parts.length < 2) {
                        System.out.println("Invalid GET request. No filename specified.");
                        out.write("Error: No filename specified\r\n");
                        out.flush();
                    } else if (parts.length == 2) {
                        // System.out.println(serveFile("test1.txt"));
                        String filename = parts[1].trim();
                        System.out.println(filename);
                        System.out.println(serveFile(filename));
                        
                        out.write(serveFile(filename) + "\r\n");
                        out.flush();
                    }

                }
                
            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                try {
                    connection.close();
                } catch (IOException e) {
                // ignore;
                }
            }
        return null;
        }
    }
    private static String collectInput(Socket connection) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "UTF-8"));
            String line = reader.readLine();
            sb.append(line);
        } catch (IOException e) {

        }
        return sb.toString();
    }
    private static String serveIndex() {
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
        return sb.toString();
    }
    private static String serveFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        Path filePath = directory.resolve(fileName);

        if (!Files.exists(filePath)) {
            System.err.println("File not found: ");
        }
        if (!Files.isReadable(filePath)) {
            return "Error: File is not readable.";
        }

        try (Scanner scanner = new Scanner(filePath)) {
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "Error: Unable to read file.";
        }
        return sb.toString();
    }
}
   