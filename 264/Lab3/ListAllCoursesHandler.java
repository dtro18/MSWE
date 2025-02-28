/**
 * @(#)ListAllCoursesHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */

import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * "List all courses" command event handler.
 */

// Change the extended class to IActivity
public class ListAllCoursesHandler implements IActivity {

    /**
     * Construct "List all courses" command event handler.
     *
     * @param objDataBase reference to the database object
     * @param iCommandEvCode command event code to receive the commands to process
     * @param iOutputEvCode output event code to send the command processing result
     */
    DBInterface dbStub;
    public ListAllCoursesHandler(DBInterface stub) {
        super();
        dbStub = stub;
    }

    /**
     * Process "List all courses" command event.
     *
     * @param param a string parameter for command
     * @return a string result of command processing
     */
    public String execute(String param) {
        // Get all course records.
        // ListAllCoursesHandler listAllCoursesHandler = new ListAllCoursesHandler(dbStub);

        ArrayList<Course> vCourse;
        String sReturn = "";
        try {
            vCourse = dbStub.getAllCourseRecords();
            // Construct a list of course information and return it.
            sReturn = "";
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