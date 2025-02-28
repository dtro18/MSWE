/**
 * @(#)ListAllStudentsHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * "List all students" command event handler.
 */
public class ListAllStudentsHandler implements IActivity {

    /**
     * Construct "List all students" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    DBInterface dbStub;
    public ListAllStudentsHandler(DBInterface stub) {
        super();
        dbStub = stub;
    }

    /**
     * Process "List all students" command event.
     *
     * @param param a string parameter for command
     * @return a string result of command processing
     */
    public String execute(String param) {
        // Get all student records.
        String sReturn = "";
        try {    
            ArrayList<Student> vStudent = dbStub.getAllStudentRecords();
            // Construct a list of student information and return it.
            for (int i=0; i<vStudent.size(); i++) {
                sReturn += (i == 0 ? "" : "\n") + ((Student) vStudent.get(i)).toString();
            }
            return sReturn;
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return sReturn;
    }
}