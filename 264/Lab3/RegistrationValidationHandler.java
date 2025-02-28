
/**
 * @(#)RegisterStudentHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;

 
 /**
  * "Register a student for a course" command event handler.
  */
 public class RegistrationValidationHandler extends UnicastRemoteObject implements IActivity {
 
     /**
      * Construct "Register a student for a course" command event handler.
      *
      * @param objDataBase reference to the database object
      * @param iCommandEvCode command event code to receive the commands to process
      * @param iOutputEvCode output event code to send the command processing result
      */
    DBInterface dbStub;
     public RegistrationValidationHandler(DBInterface stub) throws RemoteException{
         super();
         dbStub = stub;
     }
 
     /**
      * Process "Register a student for a course" command event.
      *
      * @param param a string parameter for command
      * @return a string result of command processing
      */
     public String execute(String param) {
         // Parse the parameters.
         StringTokenizer objTokenizer = new StringTokenizer(param);
         String sSID     = objTokenizer.nextToken();
         String sCID     = objTokenizer.nextToken();
         String sSection = objTokenizer.nextToken();
 
        try {

            // Get the student and course records.
            Student objStudent = dbStub.getStudentRecord(sSID);
            Course objCourse = dbStub.getCourseRecord(sCID, sSection);
            if (objStudent == null) {
                return "Error: Invalid student ID";
            }
            if (objCourse == null) {
                return "Error: Invalid course ID or course section";
            }
    
            // Check if the given course conflicts with any of the courses the student has registered.
            ArrayList<Course> vCourse = objStudent.getRegisteredCourses();
            for (int i=0; i<vCourse.size(); i++) {
                if (((Course) vCourse.get(i)).conflicts(objCourse)) {
                    return "Error: Registration conflicts";
                }
            }
 
        } catch (RemoteException e) {
            e.printStackTrace();
        }
         
         // Request validated. Proceed to register.
         return "Success: " + sSID + " " + sCID + " " + sSection;
     }
 }