import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.regex.Pattern;

class TFTheOne {


    @FunctionalInterface
    interface Chain<T, R> {
        // Executes current function in the chain
        R apply(T input);

        // Creates a new chain that composes current function with next one
        default <V> Chain<T, V> bind(Chain<R, V> next) {
            return input -> next.apply(this.apply(input));
        }
    }

    // Helper function that converts regular functions <T, R> to Chain instances
    public static <T, R> Chain<T, R> makeChain(Function<T, R> function) {
        return function::apply;
    }

    // make
    public Chain<String, String> read_file = makeChain(path_to_file -> {
        String data = "";
        try {
            data = Files.readString(Paths.get(path_to_file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    });

    public Chain<String, String> filter_chars = makeChain(data -> {
        Pattern pattern = Pattern.compile("[\\W_]+");
        data = pattern.matcher(data).replaceAll(" ").toLowerCase();
        return data;
    });

    public Chain<String, ArrayList<String>> remove_stop_words = makeChain(word_list -> {
        // Stores stopwords
        ArrayList<String> stopWords = new ArrayList<>();
        try {
            File stopWordsFile = new File("stop_words.txt");
            Scanner stopWordReader = new Scanner(stopWordsFile);
            String str = stopWordReader.nextLine();
            String[] words = str.split(",");    
            for (int i = 0; i < words.length; i++) {
                stopWords.add(words[i]);
            }
            stopWordReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 

        ArrayList<String> filteredWords = new ArrayList<>();
        String[] words = word_list.split(" ");
        for (String word : words) {
            if (stopWords.contains(word) || word.length() < 2) {
                continue;
            } else {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    });
    
    public Chain<ArrayList<String>, HashMap<String, Integer>> get_frequencies = makeChain(book_words -> {
        // Takes in a processed arrayList as input and outputs a dictionary
        HashMap<String, Integer> freq_dict = new HashMap<>();
        for (String word : book_words) {
            if (freq_dict.containsKey(word)) {
                freq_dict.put(word, freq_dict.get(word) + 1);
            }
            else {
                freq_dict.put(word, 1);
            }
        }
        return freq_dict;
    });

    public Chain<HashMap<String, Integer>, String> get_top_25 = makeChain(freq_dict -> {
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        // Iterate through the map and add entries to the PriorityQueue
        for (Map.Entry<String, Integer> entry : freq_dict.entrySet()) {
            pq.offer(entry);
            if (pq.size() > 25) {
                pq.poll(); 
            }
        }

        // Print the top k frequent items
        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> entry = pq.poll();
            result.add(entry.getKey() + " - " + entry.getValue());
        }
        
        String resString = "";
        for (int i = result.size() - 1; i > - 1; i--) {
            resString += result.get(i) + " \n";
        }
        return resString;
    });

}

public class Ten {
    public static void main(String[] args) {
        TFTheOne driver = new TFTheOne();

        String result = driver.read_file
            .bind(driver.filter_chars)
            .bind(driver.remove_stop_words)
            .bind(driver.get_frequencies)
            .bind(driver.get_top_25)
            .apply(args[0]);
        System.out.println(result);
    }
}
