import java.rmi.Remote;
import java.rmi.RemoteException;

// This interface extends java.rmi.Remote and defines the execute() method, enabling
// remote execution of tasks. It is implemented by all the handler classes from Lab 2, such as
// SWE 264P Distributed Software Architecture Winter 2025
// Lab 3: Remote Method Invocation Prof. Sam Malek
// 2
// ListStudents. Each implementation encapsulates its specific logic—for example, listing all
// students—within the execute() method


public interface IActivity extends Remote {

    // Defines execute() method, implemented by handler classes
    // Each implementation encapsulates its specific logic within execute() method
    String execute(String param) throws RemoteException;
}
