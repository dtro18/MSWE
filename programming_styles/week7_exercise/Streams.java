import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Stream;

public class Streams {
    
    public static ArrayList<String> loadStopWords() {
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
        return stopWords;
    }

    public static boolean isStopWord(ArrayList<String> stopWords, String word) {
        if (stopWords.contains(word)) {
            return true;

        }
        return false;
    }
    // line.split("[^a-zA-Z]+")
    public static void main(String args[]) {
        ArrayList<String> stopWords = loadStopWords();
        HashMap<String, Integer> freqDict = new HashMap<>();

        try (Stream<String> lines = Files.lines(Paths.get("pride-and-prejudice.txt"))) {
            lines
                .flatMap(line -> Stream.of(line.split("[^a-zA-Z]+")))
                .map(word -> word.toLowerCase())
                .filter(word -> !isStopWord(stopWords, word))
                .filter(word -> word.length() > 1)
                .forEach(word -> {
                    if (freqDict.containsKey(word)) {
                    freqDict.put(word, freqDict.get(word) + 1);
                    } else {
                        freqDict.put(word, 1);
                    }
                });

            PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
            ArrayList<String> result = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : freqDict.entrySet()) {pq.offer(entry);if (pq.size() > 25) {pq.poll();}}
            while (!pq.isEmpty()) {Map.Entry<String, Integer> entry = pq.poll();result.add(entry.getKey() + " - " + entry.getValue());}
            Collections.reverse(result);
            System.out.println(String.join(" \n", result) + " \n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
