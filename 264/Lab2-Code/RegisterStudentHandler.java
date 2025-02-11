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
public class RegisterStudentHandler extends CommandEventHandler {

    /**
     * Construct "Register a student for a course" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    public RegisterStudentHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
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
        if (param.startsWith("Error: ")) {
            return param;
        }
        // If no error:
        StringTokenizer objTokenizer = new StringTokenizer(param);
        String success  = objTokenizer.nextToken();
        String sSID     = objTokenizer.nextToken();
        String sCID     = objTokenizer.nextToken();
        String sSection = objTokenizer.nextToken();

        // Get the student and course records.
        Student objStudent = this.objDataBase.getStudentRecord(sSID);
        Course objCourse = this.objDataBase.getCourseRecord(sCID, sSection);

        // Request validated. Proceed to register.
        this.objDataBase.makeARegistration(sSID, sCID, sSection);

        int numEnrolled = objCourse.getRegisteredStudents().size();
        System.out.println("numEnrolled: " + numEnrolled);
        if (numEnrolled > 3) {
            return "Class is overbooked! Registering anyway.";
        } else{
            return "Class registered successfully.";
        }
    }
}