import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
class Pair {
    String word;
    Integer val;

    Pair(String word) {
        this.word = word;
        this.val = 1;
    }

    Pair(String word, Integer val) {
        this.word = word;
        this.val = val;
    }
}
public class ThirtyTwo {
    // Breaks string 
    public static ArrayList<String> partition(Integer nLines) {
        ArrayList<String> splits = new ArrayList<>();
        String bookWords = "";
        try {
            Scanner targetReader = new Scanner(new File("pride-and-prejudice.txt"));
            String line;
            Integer i = 0;
            
            while (targetReader.hasNextLine()) {
                line = targetReader.nextLine();
                if (line.length() == 0) {
                    continue;
                }

                String[] words = line.split("[^a-zA-Z]+");
                for (String word : words) {
                    // System.out.println(word);
                    if (word.length() > 1) {
                        bookWords += word.toLowerCase() + " " ;
                    }   
                }
                i += 1;

                if (i == nLines) {
                    // System.out.println(bookWords);
                    splits.add(bookWords);
                    bookWords = "";
                    i = 0;
                    continue;
                }
                
            }
            if (!bookWords.equals("")) {
                splits.add(bookWords);
            }
            targetReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Target file not found");
        }
        return splits;

    }

    public static ArrayList<Pair> splitWords(String dataStr) {
        // Get array of cleaned words
        String cleaned = dataStr.replaceAll("[\\W_]+", " ");
        String[] cleanedWords = cleaned.toLowerCase().trim().split("\\s+");

        Set<String> stopWords = new HashSet<>();
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

        ArrayList<Pair> result = new ArrayList<>();
        for (String word : cleanedWords) {
            if (!stopWords.contains(word) && word.length() > 1) {
                result.add(new Pair(word));
            }
        }
        return result;
    }

    // Takes list of pairLists and restructures
    // Produces a dictionary with key, value = word, (listof pairs with that word)
    public static HashMap<String, ArrayList<Pair>> regroup(List<ArrayList<Pair>> pairsList) {
        HashMap<String, ArrayList<Pair>> mapping = new HashMap<>();
        for (ArrayList<Pair> pairs : pairsList) {
            for (Pair pair : pairs) {
                String word = pair.word;
                if (mapping.containsKey(word)) {
                    mapping.get(word).add(pair);
                } else {
                    ArrayList<Pair> newList = new ArrayList<>();
                    mapping.put(word, newList);

                }
            }
        }
        return mapping;
    }

    public static Pair countWords(String word, ArrayList<Pair> mapping) {
        return new Pair(word, mapping.size() + 1);
    }


    
    public static void main(String[] args) {
        List<ArrayList<Pair>> splits = partition(50).stream().map(wordChunk -> splitWords(wordChunk)).collect(Collectors.toList());
        HashMap<String, ArrayList<Pair>> splitsPerWord = regroup(splits);

        HashMap<String, Integer> wordFreqs = new HashMap<>();
        for (Map.Entry<String, ArrayList<Pair>> entry : splitsPerWord.entrySet()) {
            String key = entry.getKey();
            ArrayList<Pair> value = entry.getValue();
            Pair updatedPair = countWords(key, value);

            wordFreqs.put(updatedPair.word, updatedPair.val);
        }

        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordFreqs.entrySet()) {pq.offer(entry);if (pq.size() > 25) {pq.poll();}}
        while (!pq.isEmpty()) {Map.Entry<String, Integer> entry = pq.poll();result.add(entry.getKey() + " - " + entry.getValue());}
        Collections.reverse(result);
        System.out.println(String.join(" \n", result) + " \n"); 

    }
}
