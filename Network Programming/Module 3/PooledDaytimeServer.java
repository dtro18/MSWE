import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
public class PooledDaytimeServer {
    public final static int PORT = 13;
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(50);
        // Serversocket listens for client connections on specified port
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try {
                    // Accept client connection. Block until client connects. 
                    Socket connection = server.accept();
                    // Wrap connection in Daytime Task (callable implementation)
                    Callable<Void> task = new DaytimeTask(connection);
                    // Submits task to thread pool
                    pool.submit(task);
                } catch (IOException ex) {}
            }
        } catch (IOException ex) {
            System.err.println("Couldn't start server");
        }
    }
    // Each daytime task represents one connection
    private static class DaytimeTask implements Callable<Void> {
        // Create that connection
        private Socket connection;
        DaytimeTask(Socket connection) {
            this.connection = connection;
        }
        @Override
        public Void call() {
            try {
                Writer out = new OutputStreamWriter(connection.getOutputStream());
                Date now = new Date();
                out.write(now.toString() +"\r\n");
                out.flush();
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
}
   