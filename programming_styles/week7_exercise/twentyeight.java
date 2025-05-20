import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

class WordIterator implements Iterator<String> {
    BufferedReader reader;
    String lastChar = "";
    String word = "";
    public WordIterator(String pathToFile) throws IOException {
        try {
            reader = new BufferedReader(new FileReader(pathToFile));
        } catch (Exception e) {
            System.out.println("Cannot read file");
            e.printStackTrace();
        }
        
    }
    
    public boolean hasNext() {
        try {
            int character = reader.read();
            // If EOF reached, see if there's a word still present
            if (character == -1) {
                return word.length() > 0;
            }
            char c = (char) character;
            if (Character.isLetter(c)) {
                lastChar = Character.toString(Character.toLowerCase(c));
                return true;
            } else {
                // Try again 
                return hasNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public String next() {
        word = lastChar;
        lastChar = "";
        try {
            int character = reader.read();
            while (Character.isLetter(character)) {
                word += Character.toString(Character.toLowerCase((char) character));
                character = reader.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String res = word;
        word = "";
        return res;
    }


}
public class twentyeight {

    // 
    static String nonStopWords(String nextWord) {
        ArrayList<String> stopWords = new ArrayList<>();
        try {
            Scanner targetReader = new Scanner(new File("stop_words.txt"));
            String line;
            String[] words;
            while (targetReader.hasNextLine()) {
                line = targetReader.nextLine();
                if (line.length() == 0) {continue;}
                words = line.split(",");
                for (String word : words) {stopWords.add(word);}
            }
            targetReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Target file not found");
        }
        // System.out.println(stopWords);
        if (stopWords.contains(nextWord)) {
            return "";
        } else {
            // System.out.println(nextWord);
            return nextWord;
        }
    }   

    static String populateFreqDict() {
        HashMap<String, Integer> freqDict = new HashMap<>();
        try {
            WordIterator wordIter = new WordIterator("pride-and-prejudice.txt");
            while (wordIter.hasNext()) {
                String word = nonStopWords(wordIter.next());
                if (word.length() > 1) {
                    // System.out.println(word);
                    if (freqDict.containsKey(word)) {
                    freqDict.put(word, freqDict.get(word) + 1);
                    } else {
                        freqDict.put(word, 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : freqDict.entrySet()) {pq.offer(entry);if (pq.size() > 25) {pq.poll();}}
        while (!pq.isEmpty()) {Map.Entry<String, Integer> entry = pq.poll();result.add(entry.getKey() + " - " + entry.getValue());}
        Collections.reverse(result);
        return String.join(" \n", result) + " \n";
    }
    public static void main(String[] args) {
        System.out.println(populateFreqDict());
        
    }
}