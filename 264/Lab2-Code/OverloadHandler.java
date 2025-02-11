/**
 * @(#)RegisterStudentHandler.java
 *
 * Copyright: Copyright (c) 2003,2004 Carnegie Mellon University
 *
 */
// Todo: Change subscription event to respond when ListCoursesRegistered handler?
 import java.util.ArrayList;
 import java.util.StringTokenizer;
 
 
 /**
  * "Overload Handler" command event handler.
  * Picks up on a new event triggered after registration. Passes on the overload message. 
  */
 public class OverloadHandler extends CommandEventHandler {
 
     /**
      * Construct "Register a student for a course" command event handler.
      *
      * @param objDataBase reference to the database object
      * @param iCommandEvCode command event code to receive the commands to process
      * @param iOutputEvCode output event code to send the command processing result
      */
     public OverloadHandler(DataBase objDataBase, int iCommandEvCode, int iOutputEvCode) {
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
         return param;
     }
 }