import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
// Helper class to hold dictionary data once acquired
class freqObj implements Comparable<freqObj>{
    String word;
    int count;
    public freqObj(String word, int count) {
        this.word = word;
        this.count = count;
    }
    @Override
    public int compareTo(freqObj otherObj) {
        return Integer.compare(otherObj.count, this.count);
    }
    @Override
    public String toString() {
        return this.word +
                "  -  " +
                Integer.toString(this.count);
    }
}
// FreqCount called with 
public class FreqCount {    
    
    // args[0] = target.txt args[1] = wordlist.txt
    public static void main(String[] args) {
        Set<String> stopWords = new HashSet<>();

        // Populate stopWords
        try {
            File stopWordsFile = new File(args[1]);
            Scanner stopWordReader = new Scanner(stopWordsFile);
            String str;
            str = stopWordReader.nextLine();
            String[] words = str.split(",");    
            for (String word : words) {
                stopWords.add(word);
            }
            stopWordReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        // Handle this: "Certainly," replied Elizabeth--"there are such people, but I hope I
        // Populate freqDict
        HashMap<String, Integer> freqDict = new HashMap<>();
        try {
            File targetText = new File(args[0]);
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
