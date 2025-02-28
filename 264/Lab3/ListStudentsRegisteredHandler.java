/**
 * @(#)ListStudentsRegisteredHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.rmi.server.UnicastRemoteObject;


/**
 * "List students who registered for a course" command event handler.
 */
public class ListStudentsRegisteredHandler extends UnicastRemoteObject implements IActivity {

    /**
     * Construct "List students who registered for a course" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    DBInterface dbStub;
    public ListStudentsRegisteredHandler(DBInterface stub) throws RemoteException{
        super();
        dbStub = stub;
    }

    /**
     * Process "List students who registered for a course" command event.
     *
     * @param param a string parameter for command
     * @return a string result of command processing
     */
    public String execute(String param) {
        // Parse the parameters.
        StringTokenizer objTokenizer = new StringTokenizer(param);
        String sCID     = objTokenizer.nextToken();
        String sSection = objTokenizer.nextToken();
        String sReturn = "";
        try {
            // Get the list of students who registered for the given course.
            Course objCourse = dbStub.getCourseRecord(sCID, sSection);
            if (objCourse == null) {
                return "Invalid course ID or course section";
            }
            ArrayList<Student> vStudent = objCourse.getRegisteredStudents();
            // Construct a list of student information and return it.
            
            for (int i=0; i<vStudent.size(); i++) {
                sReturn += (i == 0 ? "" : "\n") + ((Student) vStudent.get(i)).toString();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        return sReturn;
    }
}