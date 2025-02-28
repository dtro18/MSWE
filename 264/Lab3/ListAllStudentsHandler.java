/**
 * @(#)ListAllStudentsHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * "List all students" command event handler.
 */
public class ListAllStudentsHandler extends UnicastRemoteObject implements IActivity {

    /**
     * Construct "List all students" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    DBInterface dbStub;
    public ListAllStudentsHandler(DBInterface stub) throws RemoteException{
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
            String vStudent = dbStub.getAllStudentRecords();
            return vStudent;
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return sReturn;
    }
}