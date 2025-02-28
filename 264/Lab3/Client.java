// Client.java: This client-side component is responsible for interfacing with remote objects based
// on user input. It incorporates a command line interface (CLI) that allows users to invoke specific
// remote methods. Please note that the printing and logging should be done on the client side.

// Should call IActivity Interface

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.io.IOException;
// import java.util.StringTokenizer;

public class Client {
    public static void main(String[] args) { 
        try {
            // Create a buffered reader using system input stream.
            BufferedReader objReader = new BufferedReader(new InputStreamReader(System.in));
            Registry registry = LocateRegistry.getRegistry(null, 1100);

            // Create dual output stream to write to logfile every time System.out is printed to
            String filePath = "logs.txt";
            PrintStream fileOut = new PrintStream(
                new BufferedOutputStream(new FileOutputStream(filePath, true)), true
            );
            PrintStream consoleOut = System.out;

            PrintStream dualOut = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    consoleOut.write(b); // Print to console
                    fileOut.write(b);    // Print to file
                }
            });
            System.setOut(dualOut);
            System.setErr(dualOut);

            while (true) {
                
                // Show available commands and get a choice.
                System.out.println("\nStudent Registration System\n");
                System.out.println("1) List all students");
                System.out.println("2) List all courses");
                System.out.println("3) List students who registered for a course");
                System.out.println("4) List courses a student has registered for");
                System.out.println("5) List courses a student has completed");
                System.out.println("6) Register a student for a course");
                System.out.println("x) Exit");
                System.out.println("\nEnter your choice and press return >> ");
                String sChoice = objReader.readLine().trim();
                // Execute command 1: List all students.
                if (sChoice.equals("1")) {
                    // Remote call
                    IActivity stub = (IActivity) registry.lookup("ListAllStudentsHandler");
                    String students = stub.execute("");
                    System.out.println(students);
                }

                // Execute command 2: List all courses.
                if (sChoice.equals("2")) {
                    IActivity stub = (IActivity) registry.lookup("ListAllCoursesHandler");
                    String courses = stub.execute("");
                    System.out.println(courses);
                }

                // Execute command 3: List students registered for a course.
                if (sChoice.equals("3")) {
                    // Get course ID and course section from user.
                    System.out.println("\nEnter course ID and press return >> ");
                    String sCID = objReader.readLine().trim();
                    System.out.println("\nEnter course section and press return >> ");
                    String sSection = objReader.readLine().trim();

                    // Get the list of students who registered for the given course.
                    IActivity stub = (IActivity) registry.lookup("ListStudentsRegisteredHandler");
                    String students = stub.execute(sCID + " " + sSection);

                    System.out.println(students);
                    }


                // // Execute command 4: List courses a student has registered for.
                if (sChoice.equals("4")) {
                    // Get student ID from user.
                    System.out.println("\nEnter student ID and press return >> ");
                    String sSID = objReader.readLine().trim();

                    // Get the list of courses the given student has registered for.
                    IActivity stub = (IActivity) registry.lookup("ListCoursesRegisteredHandler");
                    String courses = stub.execute(sSID);


                    // Student objStudent = stub.getStudentRecord(sSID);
                    if (courses == "Invalid student ID") {
                        System.out.println("Invalid student ID");
                        continue;
                    }
        
                    System.out.println(courses);
                }

                // // Execute command 5: List courses a student has completed.
                if (sChoice.equals("5")) {
                    // Get student ID from user.
                    System.out.println("\nEnter student ID and press return >> ");
                    String sSID = objReader.readLine().trim();

                    IActivity stub = (IActivity) registry.lookup("ListCoursesCompletedHandler");
                    String courses = stub.execute(sSID);
                    System.out.println(courses);
                    continue;
                }

                // Execute command 6: Register a student for a course.
                if (sChoice.equals("6")) {
                    // Get student ID, course ID, and course    section from user.
                    System.out.println("\nEnter student ID and press return >> ");
                    String sSID = objReader.readLine().trim();
                    System.out.println("\nEnter course ID and press return >> ");
                    String sCID = objReader.readLine().trim();
                    System.out.println("\nEnter course section and press return >> ");
                    String sSection = objReader.readLine().trim();

                    // Grab the stub and execute the function
                    IActivity stubValidation = (IActivity) registry.lookup("RegistrationValidationHandler");
                    String validationResult = stubValidation.execute(sSID + " " +  sCID + " " + sSection);
                    IActivity stubRegister = (IActivity) registry.lookup("RegisterStudentHandler");
                    String registerResult = stubRegister.execute(validationResult);
                    System.out.println(registerResult);
                    continue;
                }

                // Terminate this client.
                if (sChoice.equalsIgnoreCase("X")) {
                    break;
                }
            }
            fileOut.close();

            // Clean up the resources.
            objReader.close();
        }
        catch (Exception e) {
            // Dump the exception information for debugging.
            e.printStackTrace();
            System.exit(1);
        }
    }
}
