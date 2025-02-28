/**
 * @(#)RegisterStudentHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */
// Todo: Change subscription event to respond when ListCoursesRegistered handler?

 /**
  * "Overload Handler" command event handler.
  * Picks up on a new event triggered after registration. Passes on the overload message. 
  */
 public class OverloadHandler implements IActivity {
 
     /**
      * Construct "RegistWer a student for a course" command event handler.
      *
      * @param objDataBase reference to the database object
      * @param iCommandEvCode command event code to receive the commands to process
      * @param iOutputEvCode output event code to send the command processing result
      */
    DBInterface dbStub;
     public OverloadHandler(DBInterface stub) {
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
         return param;
     }
 }