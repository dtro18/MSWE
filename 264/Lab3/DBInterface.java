// This is a Java interface that extends java.rmi.Remote. It defines the methods
// required for remote database operations, such as getAllStudentRecords() and
// makeARegistration(). These methods mirror the functionality of the original database object used
// in Lab 2. Instances of classes implementing this interface will be passed as arguments to the
// constructors of all handler classes during runtime.

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface DBInterface extends Remote {
    String getAllStudentRecords() throws RemoteException;
    String getAllCourseRecords() throws RemoteException;
    Student getStudentRecord(String sSID) throws RemoteException;
    String getStudentName(String sSID) throws RemoteException;
    Course getCourseRecord(String sCID, String sSection) throws RemoteException;
    String getCourseName(String sCID) throws RemoteException;
    void makeARegistration(String sSID, String sCID, String sSection) throws RemoteException;

}