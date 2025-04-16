// This one was annoying.

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

        ArrayList<String> wordList = new ArrayList<>();
        ArrayList<Integer> freqList = new ArrayList<>();

        try {
            File targetText = new File(args[1]);
            Scanner targetReader = new Scanner(targetText);
            String line;

            // Outer loop through file
            while (targetReader.hasNextLine()) {
                // Add space so the last word is processed
                line = targetReader.nextLine() + " ";
            
                // If line is empty
                if (line.length() == 0) {
                    continue;
                }

                // If line is not empty
                Integer start_char = null; // Will store current word's start index
                Integer j = 0;
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (start_char == null) {
                        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                            start_char = j;
                        }
                    } else {
                        if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                            boolean found = false;
                            String word = "";
                            // Grab the whole word
                            for (int k = start_char; k < j; k++) {

                                word += Character.toLowerCase(line.charAt(k));
                            }
                            // Check if word in stopWords
                            boolean isStopWord = false;
                            for (int m = 0; m < stopWordsList.size(); m ++) {
                                if (word.equals(stopWordsList.get(m))) {
                                    isStopWord = true;
                                }
                            }
                            
                            // If the word is not a stopword, proceed.
                            if (!isStopWord && word.length() >= 2) {
                                Integer insertedWordIdx = 0;
                                for (int l = 0; l < wordList.size(); l ++) {
                                    if (word.equals(wordList.get(l))) {
                                        freqList.set(l, freqList.get(l) + 1);
                                        insertedWordIdx = l;
                                        found = true;
                                        break;

                                    } 
                                }
                                if (!found) {
                                    wordList.add(word);
                                    freqList.add(1);
                                    insertedWordIdx = wordList.size() - 1;
                                }
                                if (wordList.size() > 1) {
                                    // Bubblesort desc both arrs
                                    for (int o = insertedWordIdx; o > -1; o--) {
                                        if (freqList.get(o) < freqList.get(insertedWordIdx)) {
                                            Integer tempFreq = freqList.get(o);
                                            freqList.set(o, freqList.get(insertedWordIdx));
                                            freqList.set(insertedWordIdx, tempFreq);

                                            String tempWord = wordList.get(o);
                                            wordList.set(o, wordList.get(insertedWordIdx));
                                            wordList.set(insertedWordIdx, tempWord);

                                            insertedWordIdx = o;
                                        }
                                    }
                                }
                            }
                            start_char = null;
                        }
                        
                    }
                    j += 1;
                }
            }
        for (int i = 0; i < 25; i++ ) {
            System.out.println(wordList.get(i));
            System.out.println(freqList.get(i));
        }
        // System.out.println(wordList);
        } catch (FileNotFoundException e) {
            System.out.println("Target file not found");
        }
    }

}
