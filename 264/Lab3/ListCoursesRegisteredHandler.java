/**
 * @(#)ListCoursesRegisteredHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * "List courses a student has registered for" command event handler.
 */
public class ListCoursesRegisteredHandler implements IActivity {

    /**
     * Construct "List courses a student has registered for" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    DBInterface dbStub;
    public ListCoursesRegisteredHandler(DBInterface stub) {
        super();
        dbStub = stub;
    }

    /**
     * Process "List courses a student has registered for" command event.
     *
     * @param param a string parameter for command
     * @return a string result of command processing
     */
    public String execute(String param) {
        // Parse the parameters.
        StringTokenizer objTokenizer = new StringTokenizer(param);
        String sSID = objTokenizer.nextToken();
        String sReturn = "";
        // Get the list of courses the given student has registered for.
        try {
            Student objStudent = dbStub.getStudentRecord(sSID);
            if (objStudent == null) {
                return "Invalid student ID";
            }
            ArrayList<Course> vCourse = objStudent.getRegisteredCourses();

            // Construct a list of course information and return it.
            
            for (int i=0; i<vCourse.size(); i++) {
                sReturn += (i == 0 ? "" : "\n") + ((Course) vCourse.get(i)).toString();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return sReturn;
    }
}