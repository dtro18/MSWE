import java.util.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Nine {
    // Reads in book words and calls the next function
    static String readBookWords(Function<String, String> next) {
        String path_to_file = "pride-and-prejudice.txt";
        String data = "";
        try {
            data = Files.readString(Paths.get(path_to_file));
            Pattern pattern = Pattern.compile("[\\W_]+");
            data = pattern.matcher(data).replaceAll(" ").toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return next.apply(data);
    }

    static Function<String, ArrayList<String>> removeStopWords = wordList -> {

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
        String[] words = wordList.split(" ");
        for (String word : words) {
            if (stopWords.contains(word) || word.length() < 2) {
                continue;
            } else {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    };

    // Takes in a processed arrayList as input and outputs a dictionary
    static Function<ArrayList<String>, HashMap<String, Integer>> populateFreqDict = bookWords -> {
        HashMap<String, Integer> freqDict = new HashMap<>();
        for (String word : bookWords) {
            if (freqDict.containsKey(word)) {
                freqDict.put(word, freqDict.get(word) + 1);
            }
            else {
                freqDict.put(word, 1);
            }
        }
        return freqDict;
    };
    static Function<HashMap<String, Integer>, String> printTop25 = freqDict -> {
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        // Iterate through the map and add entries to the PriorityQueue
        for (Map.Entry<String, Integer> entry : freqDict.entrySet()) {
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
    };

    public static void main(String[] args) {

        Function<String, String> kickForward = 
            removeStopWords
                .andThen(populateFreqDict)
                .andThen(printTop25);

        String result = readBookWords(kickForward);
        System.out.println(result);
    }
}
