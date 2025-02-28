// This server-side component is responsible for instantiating all handler objects from
// Lab 2 that implement the IActivity interface and binding them to a dedicated registry.
// Additionally, it performs a lookup to retrieve the DBInterface instance through a separate registry.
// This registry is created and initialized within the main method of the Database.java class. Notably,
// the registries for the handler objects and the Database are distinct entities, each operating on
// different port numbers

// import java.io.BufferedReader;
// import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
// import java.util.ArrayList;


public class Server extends UnicastRemoteObject {
    
    public Server() throws RemoteException {
        super();
        
    }
    public static void main(String[] args) {
        // Create a buffered reader using system input stream.
        try {
            Server server = new Server();
            Registry serverRegistry = LocateRegistry.createRegistry(1100);
            serverRegistry.rebind("Server", server);
            System.out.println("Server ready");
        
            Registry dbRegistry = LocateRegistry.getRegistry(null, 1099);
            DBInterface dbStub = (DBInterface) dbRegistry.lookup("Server");
            System.out.println("Error finding registry.");

            ListAllCoursesHandler listAllCoursesHandler = new ListAllCoursesHandler(dbStub);
            ListAllStudentsHandler listAllStudentsHandler = new ListAllStudentsHandler(dbStub);
            // Calling execute on this requires params...
            ListCoursesCompletedHandler listCoursesCompletedHandler = new ListCoursesCompletedHandler(dbStub);
            ListCoursesRegisteredHandler listCoursesRegisteredHandler = new ListCoursesRegisteredHandler(dbStub);
            ListStudentsRegisteredHandler listStudentsRegisteredHandler = new ListStudentsRegisteredHandler(dbStub);
            OverloadHandler overloadHandler = new OverloadHandler(dbStub);
            // Add course conflict logic to this
            RegisterStudentHandler registerStudentHandler = new RegisterStudentHandler(dbStub);
            RegistrationValidationHandler registrationValidationHandler = new RegistrationValidationHandler(dbStub);


        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
    }
}
