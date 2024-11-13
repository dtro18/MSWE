import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

// The tutorial can be found just here on the SSaurel's Blog : 
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// Each Client Connection will be managed in a dedicated Thread
public class WebServer { 
	
    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";

	// Executor framework - producer/consumer
	// Decouple task submission from task execution
	private static final int NTHREADS = 100; 
    private static final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS); 

    // port to listen connectionS
    static final int PORT = 8080;
	
    // verbose mode
    static final boolean verbose = true;
	
    // Client Connection via Socket Class
    private Socket connect;
	
    public WebServer(Socket c) {
	connect = c;
    }
	
    public static void main(String[] args) {
		ServerSocket serverConnect = null;
		try {
			// ServerSocket object that sits and waits for connections
			serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
				
			// we listen for connections on a specified port (8080)
			while (true) {
				// When something connects to the ServerSocket object (serverConnect), a new socket object is made. This socket object represents the unique hclient-server connection.
				// WebServer manages all the communication with a specific client using its given socket.
				// Multithreading comes in here. Threads are assigned for a single client's socket.
				WebServer myServer = new WebServer(serverConnect.accept());
				
				if (verbose) {
					System.out.println("Connection opened. (" + new Date() + ")");
					
				}
				// runnables are just tasks that can be executed by a thread
				// Defines what happens when a thread runs
				Runnable task = new Runnable() {
					public void run() {
						// Debugging 
						System.out.println("Handling request in thread: " + Thread.currentThread().getName() + " (ID: " + Thread.currentThread().getName() + ")");
						// What should happen when the thread runs. We tell the webserver to handle it.
						myServer.handleRequest();
					}
				};
				// exec manages the pool of threads, handling tasks in separate threads.
				// tell exec to pick a thread from its pool and run the task
				exec.execute(task);
				// myServer.handleRequest();
			}
				
		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		} finally {
			exec.shutdown();
			if (serverConnect != null && !serverConnect.isClosed()) {
				try {
					serverConnect.close();
				} catch(IOException e){
					System.err.println("Error closing server socket.");
				}
			}
			
		}
    }

    public void handleRequest() {
	// we manage our particular client connection
	BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
	String fileRequested = null;
		
	try {
	    // we read characters from the client via input stream on the socket
	    in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
	    // we get character output stream to client (for headers)
	    out = new PrintWriter(connect.getOutputStream());
	    // get binary output stream to client (for requested data)
	    dataOut = new BufferedOutputStream(connect.getOutputStream());
			
	    // get first line of the request from the client
	    String input = in.readLine();
	    if (input == null)
		return;
	    // we parse the request with a string tokenizer
	    StringTokenizer parse = new StringTokenizer(input);
	    String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
	    // we get file requested
	    fileRequested = parse.nextToken().toLowerCase();
	    
	    System.out.println(method + " " + fileRequested);
	    // we support only GET and HEAD methods, we check
	    if (!method.equals("GET")  &&  !method.equals("HEAD")) {
		if (verbose) {
		    System.out.println("501 Not Implemented : " + method + " method.");
		}
		
		// we return the not supported file to the client
		File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
		int fileLength = (int) file.length();
		String contentMimeType = "text/html";
		//read content to return to client
		byte[] fileData = readFileData(file, fileLength);
					
		// we send HTTP Headers with data to client
		out.println("HTTP/1.1 501 Not Implemented");
		out.println("Server: Java HTTP Server from SSaurel : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + contentMimeType);
		out.println("Content-length: " + fileLength);
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer
		// file
		dataOut.write(fileData, 0, fileLength);
		dataOut.flush();
				
	    } else {
		// GET or HEAD method
		if (fileRequested.endsWith("/")) {
		    fileRequested += DEFAULT_FILE;
		}
		
		File file = new File(WEB_ROOT, fileRequested);
		int fileLength = (int) file.length();
		String content = getContentType(fileRequested);
				
		if (method.equals("GET")) { // GET method so we return content
		    byte[] fileData = readFileData(file, fileLength);
		    
		    // send HTTP Headers
		    out.println("HTTP/1.1 200 OK");
		    out.println("Server: Java HTTP Server from SSaurel : 1.0");
		    out.println("Date: " + new Date());
		    out.println("Content-type: " + content);
		    out.println("Content-length: " + fileLength);
		    out.println(); // blank line between headers and content, very important !
		    out.flush(); // flush character output stream buffer
		    
		    dataOut.write(fileData, 0, fileLength);
		    dataOut.flush();
		}
				
		if (verbose) {
		    System.out.println("File " + fileRequested + " of type " + content + " returned");
		}
		
	    }
	    
	} catch (FileNotFoundException fnfe) {
	    try {
		fileNotFound(out, dataOut, fileRequested);
	    } catch (IOException ioe) {
		System.err.println("Error with file not found exception : " + ioe.getMessage());
	    }
	    
	} catch (IOException ioe) {
	    System.err.println("Server error : " + ioe);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		in.close();
		out.close();
		dataOut.close();
		connect.close(); // we close socket connection
	    } catch (Exception e) {
		System.err.println("Error closing stream : " + e.getMessage());
	    } 
			
	    if (verbose) {
		System.out.println("Connection closed.\n");
	    }
	}
    }
	
    private byte[] readFileData(File file, int fileLength) throws IOException {
	FileInputStream fileIn = null;
	byte[] fileData = new byte[fileLength];
		
	try {
	    fileIn = new FileInputStream(file);
	    fileIn.read(fileData);
	} finally {
	    if (fileIn != null) 
		fileIn.close();
	}
		
	return fileData;
    }
	
    // return supported MIME Types
    private String getContentType(String fileRequested) {
	if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
	    return "text/html";
	else
	    return "text/plain";
    }
	
    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
	File file = new File(WEB_ROOT, FILE_NOT_FOUND);
	int fileLength = (int) file.length();
	String content = "text/html";
	byte[] fileData = readFileData(file, fileLength);
		
	out.println("HTTP/1.1 404 File Not Found");
	out.println("Server: Java HTTP Server from SSaurel : 1.0");
	out.println("Date: " + new Date());
	out.println("Content-type: " + content);
	out.println("Content-length: " + fileLength);
	out.println(); // blank line between headers and content, very important !
	out.flush(); // flush character output stream buffer
		
	dataOut.write(fileData, 0, fileLength);
	dataOut.flush();
		
	if (verbose) {
	    System.out.println("File " + fileRequested + " not found");
	}
    }
	
}
