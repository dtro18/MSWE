import java.util.*;
import java.io.*;  // Import the File class

public class Seven {
    static ArrayList<String> bookWords = new ArrayList<>();
    static HashMap<String, Integer> freqDict = new HashMap<>();
    static ArrayList<String> readStopWords(String path) {
        ArrayList<String> stopWords = new ArrayList<>();
        try {
            Scanner stopWordReader = new Scanner(new File(path));
            String str = stopWordReader.nextLine();
            String[] words = str.split(",");    
            for (int i = 0; i < words.length; i++) {stopWords.add(words[i]);}
            stopWordReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }   
        return stopWords;
    }
    static ArrayList<String> readBookWords(String path) {
        ArrayList<String> bookWords = new ArrayList<>();
        try {
            Scanner targetReader = new Scanner(new File(path));
            String line;
            String[] words;
            while (targetReader.hasNextLine()) {
                line = targetReader.nextLine();
                if (line.length() == 0) {continue;}
                words = line.split(" |--|-");
                for (String word : words) {bookWords.add(word);}
            }
            targetReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Target file not found");
        }
        return bookWords;
    }
    static ArrayList<String> cleanWords(ArrayList<String> bookWordsInput, ArrayList<String> stopWords) {
        ArrayList<String> cleaned = new ArrayList<>();
        for (int i = 0; i < bookWordsInput.size(); i++) {
            String word = bookWordsInput.get(i);
            word = word.toLowerCase().replaceAll("'s", "").replaceAll("[^a-z]", "");
            if (stopWords.contains(word) || word.length() < 2){continue;}
            else {cleaned.add(word);}
        }
        return cleaned;
    }
    static HashMap<String, Integer> populateFreqDict(ArrayList<String> bookWords) {
        HashMap<String, Integer> freqDict = new HashMap<>();
        for (String word : bookWords) {if (freqDict.containsKey(word)) {freqDict.put(word, freqDict.get(word) + 1);}else {freqDict.put(word, 1);}}
        return freqDict;
    }
    static String printTop25(HashMap<String, Integer> freqDict, Integer k) {
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : freqDict.entrySet()) {pq.offer(entry);if (pq.size() > k) {pq.poll();}}
        while (!pq.isEmpty()) {Map.Entry<String, Integer> entry = pq.poll();result.add(entry.getKey() + " - " + entry.getValue());}
        Collections.reverse(result);
        return String.join(" \n", result) + " \n";

    }
    public static void main(String[] args) {
        System.out.println(printTop25(populateFreqDict(cleanWords(readBookWords("pride-and-prejudice.txt"), readStopWords("stop_words.txt"))), 25));
    }
}
