import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Six {

    
    // Stores bookwords
    static ArrayList<String> bookWords = new ArrayList<>();

    // Adj list
    static HashMap<String, Integer> freqDict = new HashMap<>();

    // Returns list of stopWords 
    static ArrayList<String> readStopWords(String path) {
        // Stores stopwords
        ArrayList<String> stopWords = new ArrayList<>();
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
        return stopWords;
    }

    // Returns list of words in a book.
    static ArrayList<String> readBookWords(String path) {
        ArrayList<String> bookWords = new ArrayList<>();
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
        return bookWords;
    }

    // Takes in a raw book array and a stopwords array and returns a processed arraylist.
    static ArrayList<String> cleanWords(ArrayList<String> bookWordsInput, ArrayList<String> stopWords) {
        ArrayList<String> cleaned = new ArrayList<>();

        for (int i = 0; i < bookWordsInput.size(); i++) {
            String word = bookWordsInput.get(i);
            word = word.toLowerCase().replaceAll("'s", "").replaceAll("[^a-z]", "");
            if (stopWords.contains(word) || word.length() < 2){
                continue;
            }
            else {
                cleaned.add(word);
            }
        }
        return cleaned;
    }

    // Takes in a processed arrayList as input and outputs a dictionary
    static HashMap<String, Integer> populateFreqDict(ArrayList<String> bookWords) {
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
    }

    static String printTop25(HashMap<String, Integer> freqDict, Integer k) {
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        // Iterate through the map and add entries to the PriorityQueue
        for (Map.Entry<String, Integer> entry : freqDict.entrySet()) {
            pq.offer(entry);
            if (pq.size() > k) {
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
    }
    public static void main(String[] args) {
        System.out.println(printTop25(populateFreqDict(cleanWords(readBookWords("pride-and-prejudice.txt"), readStopWords("stop_words.txt"))), 25));
    }
}
