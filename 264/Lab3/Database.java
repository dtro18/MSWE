import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Database extends UnicastRemoteObject implements DBInterface {
    public ArrayList<Student> vStudent;
    public ArrayList<Course> vCourse;

    public Database() throws RemoteException {
        super();
        // Implement these?
        vStudent = new ArrayList<Student>(); // Initialize to avoid NullPointerException
        vCourse = new ArrayList<Course>();
    }
    public String getAllStudentRecords() throws RemoteException {
        String sReturn = "";
        for (Student student : vStudent) {
            sReturn += student.toString() + "\n";

        }
        return sReturn;


    }
    public ArrayList<Course> getAllCourseRecords() throws RemoteException {
        return this.vCourse;
    }
    public Student getStudentRecord(String sSID) throws RemoteException {
        for (int i=0; i<this.vStudent.size(); i++) {
            Student objStudent = (Student) this.vStudent.get(i);
            if (objStudent.match(sSID)) {
                return objStudent;
            }
        }
        // Return null if not found.
        return null;
    }
    public String getStudentName(String sSID) throws RemoteException {
        for (int i=0; i<this.vStudent.size(); i++) {
            Student objStudent = (Student) this.vStudent.get(i);
            if (objStudent.match(sSID)) {
                return objStudent.getName();
            }
        }

        // Return null if not found.
        return null;
    }

    public Course getCourseRecord(String sCID, String sSection) throws RemoteException {
        for (int i=0; i<this.vCourse.size(); i++) {
            Course objCourse = (Course) this.vCourse.get(i);
            if (objCourse.match(sCID, sSection)) {
                return objCourse;
            }
        }

        // Return null if not found.
        return null;
    }
    public String getCourseName(String sCID) throws RemoteException {
        for (int i=0; i<this.vCourse.size(); i++) {
            Course objCourse = (Course) this.vCourse.get(i);
            if (objCourse.match(sCID)) {
                return objCourse.getName();
            }
        }

        // Return null if not found.
        return null;
    }
    public void makeARegistration(String sSID, String sCID, String sSection) throws RemoteException {
        Student objStudent = this.getStudentRecord(sSID);
        Course  objCourse  = this.getCourseRecord(sCID, sSection);

        // Make a registration.
        if (objStudent != null && objCourse != null) {
            objStudent.registerCourse(objCourse);
            objCourse.registerStudent(objStudent);
        }
    }

    public static void main(String args[]) {
        try {
            Database db = new Database();

            String studentFileName, courseFileName;
		    // Check the number of parameters.
            if (args.length == 2) {
                studentFileName = args[0];
                courseFileName = args[1];
            } else {
                studentFileName = "bin/Students.txt";
                courseFileName = "bin/Courses.txt";		
            }

            // Check if input files exists.
            if (new File(studentFileName).exists() == false) {
                System.err.println("Could not find " + studentFileName);
                System.exit(1);
            }
            if (new File(courseFileName).exists() == false) {
                System.err.println("Could not find " + courseFileName);
                System.exit(1);
            }
            // Open the given student and course files.
            try {
                BufferedReader objStudentFile = new BufferedReader(new FileReader(studentFileName));
                BufferedReader objCourseFile  = new BufferedReader(new FileReader(courseFileName));

                // Populate student and course lists.
                while (objStudentFile.ready()) {
                    db.vStudent.add(new Student(objStudentFile.readLine()));
                }
                while (objCourseFile.ready()) {
                    db.vCourse.add(new Course(objCourseFile.readLine()));
                }

                // Close the student and course files.
                objStudentFile.close();
                objCourseFile.close();
            } catch (Exception e) {
                System.out.println("Error reading file");
            }
            

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Database", db);
            System.out.println("Database ready");
            System.out.println(db.vStudent.size());

        } catch (RemoteException e) {
            System.err.println("Error initializing Database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
