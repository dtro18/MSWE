import java.io.*;
import java.net.*;


public class Client {
    public static final String SERVER = "";
    public static final int PORT = 8080;
    public static final int TIMEOUT = 15000;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(SERVER, PORT);
            socket.setSoTimeout(TIMEOUT);

            // Output stream writer handles code going to server
            OutputStream out = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");

            // Input stream writer handles code going to client
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "UTF-8"));

            // Process Args
            if (args[0].equals("index")) {
                System.out.println("Printing available files: \n");
                sendRequest("INDEX", writer);
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    System.out.println(line);
                }
            // Get request and filename specified
            } else if (args.length == 2 && args[0].equals("get")) {
                System.out.println("Attempting to retrieve: " + args[1]);
                sendRequest("GET " + args[1], writer);
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    System.out.println(line);
                }
            } else {
                System.out.println("Error in CL arguments. \n");
            }
            
            // define(word, writer, reader);
            
            
            // Necessary if there's words left in the buffer
            writer.write("quit\r\n");
            writer.flush();

        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // Ignore 
                }
            }
            
        }
        
    } // End Main

    static void sendRequest(String request, Writer writer) {
        try {
            writer.write(request + "\r\n");
            writer.flush();
        } catch (IOException e) {}
    }

    static void define(String word, Writer writer, BufferedReader reader) 
        throws IOException, UnsupportedEncodingException {
            System.out.println("Attempting to define: " + word);
            writer.write("DEFINE fd-eng-fra " + word + "\r\n");
            writer.flush();
            System.out.println("Command sent for: " + word);

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.startsWith("250 ")) { // OK Response
                    return;
                } else if (line.startsWith("552 ")) {
                    System.out.println("No definition found for " + word);
                    return;
                // What is this case catching
                } else if (line.matches("\\d\\d\\d .*")) {
                    continue;
                // What is this case catching
                } else if (line.trim().equals(".")) {
                    continue;
                } else {
                    System.out.println(line);
                }

            }
    }
}
