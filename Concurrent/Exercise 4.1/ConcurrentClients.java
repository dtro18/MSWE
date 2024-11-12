import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// Program to test that the WebServer can handle multithreaded requests.
public class ConcurrentClients {

    private static final int NUM_CLIENTS = 10; // Number of concurrent clients

    public static void main(String[] args) {
        for (int i = 0; i < NUM_CLIENTS; i++) {
            // Override run function of Thread. Lambda function used to write singular abstract method of thread class.
            new Thread(() -> sendRequest()).start();
        }
    }

    private static void sendRequest() {
        try {
            URL url = new URL("http://localhost:8080");
            // Establish Connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            // Grab response from server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            System.out.println("Response: " + content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
