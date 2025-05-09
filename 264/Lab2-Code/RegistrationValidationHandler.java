
/**
 * @(#)RegisterStudentHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

 import java.util.ArrayList;
 import java.util.StringTokenizer;
 
 
 /**
  * "Register a student for a course" command event handler.
  */
 public class RegistrationValidationHandler extends CommandEventHandler {
 
     /**
      * Construct "Register a student for a course" command event handler.
      *
      * @param objDataBase reference to the database object
      * @param iCommandEvCode command event code to receive the commands to process
      * @param iOutputEvCode output event code to send the command processing result
      */
     public RegistrationValidationHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
         super(objDataBase, iCommandEvCode, iOutputEvCode);
     }
 
     /**
      * Process "Register a student for a course" command event.
      *
      * @param param a string parameter for command
      * @return a string result of command processing
      */
     protected String execute(String param) {
         // Parse the parameters.
         StringTokenizer objTokenizer = new StringTokenizer(param);
         String sSID     = objTokenizer.nextToken();
         String sCID     = objTokenizer.nextToken();
         String sSection = objTokenizer.nextToken();
 
         // Get the student and course records.
         Student objStudent = this.objDataBase.getStudentRecord(sSID);
         Course objCourse = this.objDataBase.getCourseRecord(sCID, sSection);
         if (objStudent == null) {
             return "Error: Invalid student ID";
         }
         if (objCourse == null) {
             return "Error: Invalid course ID or course section";
         }
 
         // Check if the given course conflicts with any of the courses the student has registered.
         ArrayList vCourse = objStudent.getRegisteredCourses();
         for (int i=0; i<vCourse.size(); i++) {
             if (((Course) vCourse.get(i)).conflicts(objCourse)) {
                 return "Error: Registration conflicts";
             }
         }
 
         // Request validated. Proceed to register.
         return "Success: " + sSID + " " + sCID + " " + sSection;
     }
 }