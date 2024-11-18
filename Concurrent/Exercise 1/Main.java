import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;

class MultithreadingDemo extends Thread {
    private boolean running = true;

    public void stopThread() {
        running = false;
    }
    public void run()
    {
        while(running) {
            try {
                // Displaying the thread that is running
                LocalTime now = LocalTime.now();
                System.out.println(
                    Thread.currentThread().getName()
                    + " is running," + " Time: " + now);

                Thread.sleep(2000);
            }
            catch (Exception e) {
                // Throwing an exception
                System.out.println("Exception is caught");
                break;
            }

        }

    }
}

public class Main {
    public static void main(String[] args) {
        
        boolean exitMain = false;
        Scanner scannerObj = new Scanner(System.in);
        List<MultithreadingDemo> threads = new ArrayList<>();

        while(!exitMain) {
            System.out.println("a - Create a new thread \n" +
                                "b - Stop a given thread (e.g. b 2 kills thread 2) \n" +
                                "c - Stop all threads and exit this program. \n");
            String userInput = scannerObj.nextLine();

            // Creating an instance of MultithreadingDemo
            if (userInput.equalsIgnoreCase("a")) {
                MultithreadingDemo newThread = new MultithreadingDemo();
                newThread.start();
                threads.add(newThread);
            }
            else if (userInput.startsWith("b ")) {
                String[] parts = userInput.split(" ");
                int threadId = Integer.parseInt(parts[1]);
                if (threadId >= 0 && threadId < threads.size()) {
                    MultithreadingDemo selectedThread = threads.get(threadId);
                    selectedThread.stopThread();
                }
            }
            else if (userInput.equalsIgnoreCase("c")) {
                exitMain = true;
            }

        }
        try {
            // Stops all threads if they are running
            for (MultithreadingDemo thread : threads) {
                
                thread.stopThread();
                System.out.println(thread.getName() + " stopped");
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting for threads to finish.");
        }
        scannerObj.close();
    }
}