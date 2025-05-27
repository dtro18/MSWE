
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class Thirty {
    static Set<String> _stopWords;
    static ArrayBlockingQueue<String> _bookWordsQueue;
    

    
    static class Multithreading extends Thread {
        HashMap<String, Integer> partialFreqDict;

        public Multithreading() {
            this.partialFreqDict = new HashMap<>();
        }
        public void run()
        {
            try {
                // Displaying the thread that is running
                System.out.println(
                    "Thread " + Thread.currentThread().threadId()
                    + " is running");
                String word;
                while ((word = _bookWordsQueue.poll()) != null) {
                    if (!_stopWords.contains(word)) {
                        if (this.partialFreqDict.containsKey(word)) {
                        this.partialFreqDict.put(word, this.partialFreqDict.get(word) + 1);
                        } else {
                            this.partialFreqDict.put(word, 1);
                        }
                    }
                    
                }
                // this.partialFreqDict.forEach((key, value) -> System.out.println(key + " = " + value));
            }
            catch (Exception e) {
                // Throwing an exception
                System.out.println("Exception is caught");
                e.printStackTrace();
            }
        }
    }

    static void loadStopWords() {
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
        _stopWords = stopWords;
    }   

    static void loadBookWords() {
        ArrayList<String> bookWords = new ArrayList<>();
        try {
            Scanner targetReader = new Scanner(new File("pride-and-prejudice.txt"));
            String line;
            // String[] words;
            while (targetReader.hasNextLine()) {
                line = targetReader.nextLine();
                if (line.length() == 0) {continue;}
                // Pattern pattern = Pattern.compile("[a-z]{2,}");
                // Matcher matcher = pattern.matcher(line);
                String[] words = line.split("[^a-zA-Z]+");
                // List<String> regexWords = new ArrayList<>();
                // while (matcher.find()) {
                //     regexWords.add(matcher.group());
                // }
                for (String word : words) {
                    // System.out.println(word);
                    if (word.length() > 1) {
                        bookWords.add(word.toLowerCase());
                    }
                    
                    
                }
            }
            targetReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Target file not found");
        }

        ArrayBlockingQueue<String> wordBlockingQueue = new ArrayBlockingQueue<>(bookWords.size());
        wordBlockingQueue.addAll(bookWords);
        _bookWordsQueue = wordBlockingQueue;
    }

    static void printTop25(HashMap<String, Integer> freqDict, Integer k) {
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : freqDict.entrySet()) {pq.offer(entry);if (pq.size() > k) {pq.poll();}}
        while (!pq.isEmpty()) {Map.Entry<String, Integer> entry = pq.poll();result.add(entry.getKey() + " - " + entry.getValue());}
        Collections.reverse(result);
        System.out.println(String.join(" \n", result) + " \n"); 

    }
    public static void main(String[] args) {
        loadStopWords();
        loadBookWords();

        ArrayList<Multithreading> threadList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Multithreading thread = new Thirty.Multithreading();
            thread.start();
            threadList.add(thread);
        }   
        HashMap<String, Integer> mainFreqDict = new HashMap<>();

        for (Multithreading thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
            
           for (Map.Entry<String, Integer> entry : thread.partialFreqDict.entrySet()) {
                mainFreqDict.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }
        printTop25(mainFreqDict, 25);
    }
}