public class TrafficController {
    private boolean bridgeActive = false;
    // Bridge is the shared resource.
    // All functions access bridgeActive, so only one function should be allowed to run at a time
    // Hence the synchronized keyword across all functions. Locks on the TrafficController object.
    
    public synchronized void enterLeft() throws InterruptedException{
        // Thread is put to sleep if it checks bridgeActive and it is. 
        // Will only wake up once notifyAll() is called.
        while (bridgeActive) {
            wait();
        }
        bridgeActive = true;
    }
    public synchronized void enterRight() throws InterruptedException {
        while (bridgeActive) {
            wait();
        }
        bridgeActive = true;
    }
    public synchronized void leaveLeft() {
        bridgeActive = false;
        notifyAll();
    }
    public synchronized void leaveRight() {
        bridgeActive = false;
        notifyAll();
    }

}