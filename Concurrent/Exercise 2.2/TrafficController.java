public class TrafficController {
    private boolean bridgeActive = false;

    public synchronized void enterLeft() throws InterruptedException{
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