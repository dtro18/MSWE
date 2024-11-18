import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.util.List;

public class MessageQueue {
    private static int n_ids;

    public static void main(String[] args) {

	// Blocking queue can only have one thread access it at a time.
	BlockingQueue<Message> queue = new LinkedBlockingQueue<>(10);
	// Deal with situation where multiple consumers for fewer producers.
	// Collect CL Arguments
	int consumerCount = Integer.parseInt(args[0]);
	int producerCount = Integer.parseInt(args[1]);

	// Iterate and start n consumer threads
	for (int i = 0; i < consumerCount; i++) {
		Consumer newConsumer = new Consumer(queue, n_ids++);
		new Thread(newConsumer).start();
	}

	// Store producers so that can stop them later

	List<Producer> producers = new ArrayList<>();
	// Iterate and start n prroducer threads
	for (int i = 0; i < producerCount; i++) {
		Producer newProducer = new Producer(queue, n_ids++);
		producers.add(newProducer);
		new Thread(newProducer).start();
	}
	// Let theproducers and consumers do their thing for 10 seconds
	try {
	    Thread.sleep(10000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	for (Producer producer : producers) {
		producer.stop();
	}

    }
}
