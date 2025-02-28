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
        try {
            // Using 1100 for serverRegistry
            Server server = new Server();
            Registry serverRegistry = LocateRegistry.createRegistry(1100);
            serverRegistry.rebind("Server", server);
            System.out.println("Server ready");
        
            // Using 1099 for DB registry
            Registry dbRegistry = LocateRegistry.getRegistry(null, 1099);
            DBInterface dbStub = (DBInterface) dbRegistry.lookup("Database");
            System.out.println("Database found");

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
            
            serverRegistry.rebind("ListAllStudentsHandler", listAllStudentsHandler);
            serverRegistry.rebind("ListAllCoursesHandler", listAllCoursesHandler);
            serverRegistry.rebind("ListCoursesCompletedHandler", listCoursesCompletedHandler);
            serverRegistry.rebind("ListCoursesRegisteredHandler", listCoursesRegisteredHandler);
            serverRegistry.rebind("ListStudentsRegisteredHandler", listStudentsRegisteredHandler);
            serverRegistry.rebind("OverloadHandler", overloadHandler);
            serverRegistry.rebind("RegisterStudentHandler", registerStudentHandler);
            serverRegistry.rebind("RegistrationValidationHandler", registrationValidationHandler);



        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
    }
}
