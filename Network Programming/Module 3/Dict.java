import java.io.*;
import java.net.*;


public class Dict {
    public static final String SERVER = "dict.org";
    public static final int PORT = 2628;
    public static final int TIMEOUT = 15000;

    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket(SERVER, PORT);
            socket.setSoTimeout(TIMEOUT);
            OutputStream out = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "UTF-8"));

            // Argument params can be accessed like so
            for (String word : args) {
                define(word, writer, reader);
            }
            
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
    // What does static mean in the context of functions?
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
