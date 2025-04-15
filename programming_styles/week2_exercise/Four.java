// package programming_styles.week2_exercise;

import java.util.*;
import java.io.*;
// FreqCount called with 
public class Four {    
    
    // args[0] = wordlist.txt args[1] =  target.txt
    
    public static void main(String[] args) {

        ArrayList<String> stopWordsList = new ArrayList<>();

        // Populate stopWords
        try {
            File stopWordsFile = new File(args[0]);
            Scanner stopWordReader = new Scanner(stopWordsFile);
            String str;
            str = stopWordReader.nextLine();
            String[] words = str.split(",");    
            for (String word : words) {
                stopWordsList.add(word);
            }
            stopWordReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        
        System.out.println(stopWordsList);
        // Handle this: "Certainly," replied Elizabeth--"there are such people, but I hope I
        // Create adjacency list to store word and frequency at index i
        ArrayList<String> wordList = new ArrayList<>();
        ArrayList<Integer> freqList = new ArrayList<>();

        try {
            File targetText = new File(args[0]);
            Scanner targetReader = new Scanner(targetText);
            String line;
            String[] words;
            while (targetReader.hasNextLine()) {
                line = targetReader.nextLine();
            
                // If line is empty
                if (line.length() == 0) {
                    continue;
                }

                // If line is not empty
                Integer start_char = null; // Will store current word's start index
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    System.out.println(c);

                }
                words = line.split(" |--|-");
                // Some lines are empty.
                for (String word : words) {
                    // Handle 
                    word = word.toLowerCase().replaceAll("'s", "").replaceAll("[^a-z]", "");

                    // System.out.println(word);
                    if (stopWords.contains(word) | word.length() == 0){
                        continue;
                    }
                    if (freqDict.containsKey(word)) {
                        freqDict.put(word, freqDict.get(word) + 1);
                    }
                    else {
                        freqDict.put(word, 1);
                    }
                }
            }
            targetReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Target file not found");
        }
        // Create list to hold dictionary entries and sort them
        List<freqObj> dictList = new ArrayList<freqObj>();
        for (String key : freqDict.keySet()) {
            Integer value = freqDict.get(key);
            freqObj newFreqObj = new freqObj(key, value);
            dictList.add(newFreqObj);
        }
        Collections.sort(dictList);
        // Print in formatted descending order: e.g. mr  -  786
        for (int i = 0; i < 25; i++) {
            freqObj dictEntryObj = dictList.get(i);
            System.out.println(dictEntryObj.toString());
        }
    }

}
