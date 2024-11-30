import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentHashMap;

public class FrequencyCount {
	// Create thread pool to deal with the text files.
	// Implement dynamic allocation
	private static final int NTHREADS = 100; 
    private static final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS); 

	private static final ConcurrentHashMap<String, Integer> masterCounter = new ConcurrentHashMap<String, Integer>(); // Maps text files to line counts
    static final class Counter {
        private int lineCount = 0;

        // Called from countLines. 
        private void process(Path filepath) {
            // Exception executes if file has some reading problem
            try (Stream<String> lines = Files.lines(filepath)){
                lineCount += lines.count();
            } catch (IOException e) {
                System.out.println("Exception: Error reading file" + filepath);
                e.printStackTrace();
            }
        }
        // Allow merging of a text file's lineCount with the masterCounter. No need to synchronize this.
        public void merge(Path filepath) {
            masterCounter.put(filepath.toString().replaceAll("^[^a-zA-Z0-9]+", ""), lineCount);
        }

        // Print method that converts the masterCounter to a printable string.
        public static String masterCounterToString() {
            StringBuilder sb = new StringBuilder("---------- Line counts -----------\n");
            masterCounter.forEach((key, value) -> sb.append(key).append(":").append(value).append("\n"));
            return sb.toString();
        }

    }

    // Countlines only called if the file exists and is readable
    private static void countLines(Path p, Counter c) {
        System.out.println("Started " + p);
        c.process(p);
        System.out.println("Ended " + p);
    }
        

    public static void main(String[] args) {
		
		try {
            // Obtain a string of the CL arguments representing a path
            // Use map (a stream method) to convert the string strem to a path stream
            Stream<Path> paths = Arrays.stream(args).map(pathString -> Paths.get(pathString));
			paths.forEach(p -> {
				Runnable task = new Runnable() {
					public void run() {
						// Give each thread its own counter
                        Counter c = new Counter();
                        if (Files.exists(p) && Files.isReadable(p)) {
                            try {
                                countLines(p, c);
                                // Merge the thread's counter with the masterCounter
                                c.merge(p);
                            } catch (Exception e) {
                                System.err.println("Error processing file: " + p);
                            }
                        } else {
                            System.err.println("File is either missing or unreadable: " + p);
                        }
					}
				};
				exec.execute(task);
			}); 		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Call this when all tasks have been scheduled
			exec.shutdown();
			try {
				if (!exec.awaitTermination(60, TimeUnit.SECONDS)) {
					exec.shutdownNow(); // Force shutdown if tasks are still running after 60 seconds
				}
			} catch (InterruptedException e) {
				exec.shutdownNow();
			}
		}

        Counter c = new Counter();
		System.out.println(c.masterCounterToString());
    }
}