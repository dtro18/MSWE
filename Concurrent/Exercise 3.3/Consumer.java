import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Consumer implements Runnable {
	private BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
    private int id;

    public Consumer(BlockingQueue<Message> q, int n) {
	queue = q;
	id = n;
    }
    
    public void run() {
	Message msg = null;
	int count = 0;
	do {
	    try {
		// Take is a blocking call. If the queue is empty, it will wait.
		msg = queue.take(); // Get a message from the queue
		count++;
		RandomUtils.print("Consumed " + msg.get(), id);
		Thread.sleep(RandomUtils.randomInteger());
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	} while (msg.get() != "stop");
	msg = new Message("stop");

	// When a consumer eats a stop message, propagate it back into the queue.
	try {
		queue.put(msg);
	} catch(InterruptedException e) {
		e.printStackTrace();
	}

	// Don't count the "stop" message
	count--;
	RandomUtils.print("Messages received: " + count, id);
    }
}
