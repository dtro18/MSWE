import java.util.concurrent.*;

public class Main3 {
    public static Semaphore semaphore = new Semaphore(1);

    private static void nap(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void addProc(HighLevelDisplay d) {

	// Add a sequence of addRow operations with short random naps.
        for(int i=0; i < 20; i++) {
            try {
                semaphore.acquire();
                System.out.println("Add semaphore acquired.");
                d.addRow("LAX FLIGHT #"+i);
                semaphore.release();
                System.out.println("Add semaphore released.");
            } catch(InterruptedException e) {
                e.printStackTrace();
            } finally {
                nap(250);
            }
        }

   }

    private static void deleteProc(HighLevelDisplay d) {
	
	// Add a sequence of deletions of row 0 with short random naps.
        for (int i = 0; i < 10; i++) {  // Limit the iterations to avoid infinite loop
            try {
                semaphore.acquire();
                System.out.println("Delete semaphore acquired.");
                d.deleteRow(0);
                semaphore.release();
                System.out.println("Delete semaphore released.");

            } catch(InterruptedException e) {
                e.printStackTrace();
            } finally {
                nap(2000);

            }
        }
            
    }

    public static void main(String [] args) {
        final HighLevelDisplay d = new JDisplay2();
        

        new Thread () {
            public void run() {
                addProc(d);
            }
        }.start();

        new Thread () {
            public void run() {
                deleteProc(d);
            }
        }.start();

    }
}