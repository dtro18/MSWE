import java.util.concurrent.*;

public class Main3 {

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
            d.addRow("LOS ANGELES FLIGHT "+i);
            nap(250);
            d.addRow("MOZAMBIQUE FLIGHT  "+i);
            nap(250);
            d.addRow("BARBADOS FLIGHT  "+i);
            nap(250);
            d.addRow("SYDNEY FLIGHT  "+i);
            nap(250);
            d.addRow("DENVER FLIGHT  "+i);
            nap(250);
            
        }

   }

    private static void deleteProc(HighLevelDisplay d) {
	
	// Add a sequence of deletions of row 0 with short random naps.
        while(true){
            nap(3000);
            d.deleteRow(0);
            
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