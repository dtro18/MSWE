import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Five {

    // Stores stopwords
    static ArrayList<String> stopWords = new ArrayList<>();
    // Stores bookwords
    static ArrayList<String> bookWords = new ArrayList<>();

    // Adj list
    static HashMap<String, Integer> freqDict = new HashMap<>();

    static void readStopWords(String path) {
        try {
            File stopWordsFile = new File(path);
            Scanner stopWordReader = new Scanner(stopWordsFile);
            String str;
            str = stopWordReader.nextLine();
            String[] words = str.split(",");    
            for (int i = 0; i < words.length; i++) {
                stopWords.add(words[i]);
            }
            stopWordReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    static void readBookWords(String path) {
        try {
            File targetText = new File(path);
            Scanner targetReader = new Scanner(targetText);
            String line;
            String[] words;
            while (targetReader.hasNextLine()) {
                line = targetReader.nextLine();
                if (line.length() == 0) {
                    continue;
                }
                words = line.split(" |--|-");
                // Some lines are empty.
                for (String word : words) {
                    bookWords.add(word);
                }
            }
            targetReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Target file not found");
        }
    }

    static void cleanWords() {
        ArrayList<String> cleaned = new ArrayList<>();

        for (int i = 0; i < bookWords.size(); i++) {
            String word = bookWords.get(i);
            word = word.toLowerCase().replaceAll("'s", "").replaceAll("[^a-z]", "");
            if (stopWords.contains(word) || word.length() < 2){
                continue;
            }
            else {
                cleaned.add(word);
            }
        }
        bookWords = cleaned;
    }

    static void populateFreqDict() {
        for (String word : bookWords) {
            if (freqDict.containsKey(word)) {
                freqDict.put(word, freqDict.get(word) + 1);
            }
            else {
                freqDict.put(word, 1);
            }
        }
    }

    static void printTop25(Integer k) {
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        String[] result = new String[k];
        // Iterate through the map and add entries to the PriorityQueue
        for (Map.Entry<String, Integer> entry : freqDict.entrySet()) {
            pq.offer(entry);
            if (pq.size() > k) {
                pq.poll(); 
            }
        }

        // Print the top k frequent items
        Integer i = 0;
        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> entry = pq.poll();
            result[i] = entry.getKey() + " - " + entry.getValue();
            i++;
        }

        for (int j = result.length - 1; j > -1; j --) {
            System.out.println(result[j]);
        }
    }
    public static void main(String[] args) {
        readStopWords("stop_words.txt");
        readBookWords("pride-and-prejudice.txt");
        cleanWords();
        populateFreqDict();
        printTop25(25);
    }
}
