import java.net.*;
import java.text.*;
import java.util.Date;
import java.io.*;


public class Test {
    // What does "throws" do
    public static Date getDateFromNetwork() throws IOException, ParseException {
        try (Socket socket = new Socket("time.nist.gov", 13)) {
            // What happens if connection is allowed but socket takes too long to respond
            socket.setSoTimeout(15000);
            InputStream in = socket.getInputStream();
            StringBuilder time = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(in, "ASCII");
            for (int c = reader.read(); c != -1; c = reader.read()) {
                // Does this specify what the type of the data to be added is?
                time.append((char) c);
            }
            System.out.println(time);
            return parseDate(time.toString());
        }
        
    }
    static Date parseDate(String s) throws ParseException {
        String[] pieces = s.split(" ");
        String dateTime = pieces[1] + " " + pieces[2] + " UTC";
        DateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss z");
        return format.parse(dateTime);
    }

    public static void main(String[] args) throws IOException, ParseException {
        Date d = Test.getDateFromNetwork();
        System.out.println("It is " + d);
        }
       
}
