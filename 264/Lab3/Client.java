// Client.java: This client-side component is responsible for interfacing with remote objects based
// on user input. It incorporates a command line interface (CLI) that allows users to invoke specific
// remote methods. Please note that the printing and logging should be done on the client side.

// Should call IActivity Interface

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
// import java.util.StringTokenizer;

public class Client {
    public static void main(String[] args) { 
        try {
            // Create a buffered reader using system input stream.
            BufferedReader objReader = new BufferedReader(new InputStreamReader(System.in));
            Registry registry = LocateRegistry.getRegistry(null, 1100);
            

            // Structure to call a method
            // String response = stub.sayHello();
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
                // TODO: Implement the rest of the user choices
                // TODO: Change return values for getAllCourseRecords
                // TODO: Deal with registration validation
                // Execute command 2: List all courses.
                // if (sChoice.equals("2")) {
                //     // Remote call
                //     ArrayList<Course> courses = stub.getAllCourseRecords();
                // }

                // // Execute command 3: List students registered for a course.
                // if (sChoice.equals("3")) {
                //     // Get course ID and course section from user.
                //     System.out.print("\nEnter course ID and press return >> ");
                //     String sCID = objReader.readLine().trim();
                //     System.out.print("\nEnter course section and press return >> ");
                //     String sSection = objReader.readLine().trim();

                //     // Get the list of students who registered for the given course.
                //     Course objCourse = stub.getCourseRecord(sCID, sSection);
                //     if (objCourse == null) {
                //         System.out.println("Invalid course ID or course section");
                //         continue;
                //     } else {
                //         ArrayList<Student> vStudent = objCourse.getRegisteredStudents();

                //         // Construct a list of student information and return it.
                //         String sReturn = "";
                //         for (int i=0; i<vStudent.size(); i++) {
                //             sReturn += (i == 0 ? "" : "\n") + ((Student) vStudent.get(i)).toString();
                //         }
                //         System.out.println(sReturn);
                //         continue;
                //     }

                    
                // }

                // // Execute command 4: List courses a student has registered for.
                // if (sChoice.equals("4")) {
                //     // Get student ID from user.
                //     System.out.println("\nEnter student ID and press return >> ");
                //     String sSID = objReader.readLine().trim();

                //     // Get the list of courses the given student has registered for.
                //     Student objStudent = stub.getStudentRecord(sSID);
                //     if (objStudent == null) {
                //         System.out.println("Invalid student ID");
                //         continue;
                //     }

                //     ArrayList<Course> vCourse = objStudent.getRegisteredCourses();

                //     // Construct a list of course information and return it.
                //     String sReturn = "";
                //     for (int i=0; i<vCourse.size(); i++) {
                //         sReturn += (i == 0 ? "" : "\n") + ((Course) vCourse.get(i)).toString();
                //     }
        
                //     System.out.println(sReturn);
                // }

                // // Execute command 5: List courses a student has completed.
                // if (sChoice.equals("5")) {
                //     // Get student ID from user.
                //     EventBus.announce(EventBus.EV_SHOW, "\nEnter student ID and press return >> ");
                //     String sSID = objReader.readLine().trim();

                //     // Announce the command event #5 with student ID.
                //     EventBus.announce(EventBus.EV_SHOW, "\n");
                //     EventBus.announce(EventBus.EV_LIST_COURSES_COMPLETED, sSID);
                //     continue;
                // }

                // // Execute command 6: Register a student for a course.
                // if (sChoice.equals("6")) {
                //     // Get student ID, course ID, and course section from user.
                //     EventBus.announce(EventBus.EV_SHOW, "\nEnter student ID and press return >> ");
                //     String sSID = objReader.readLine().trim();
                //     EventBus.announce(EventBus.EV_SHOW, "\nEnter course ID and press return >> ");
                //     String sCID = objReader.readLine().trim();
                //     EventBus.announce(EventBus.EV_SHOW, "\nEnter course section and press return >> ");
                //     String sSection = objReader.readLine().trim();

                //     // Announce the command event #5 with student ID, course ID, and course section.
                //     EventBus.announce(EventBus.EV_SHOW, "\n");
                //     EventBus.announce(EventBus.EV_REGISTER_STUDENT, sSID + " " + sCID + " " + sSection);
                //     continue;
                // }

                // Terminate this client.
                if (sChoice.equalsIgnoreCase("X")) {
                    break;
                }
            }

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
