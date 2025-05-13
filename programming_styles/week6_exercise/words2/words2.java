import java.util.*;
import java.io.*;  // Import the File class

public class words2 implements IWords{
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
            if (stopWords.contains(word) || word.length() < 2 || !word.contains("z")){continue;}
            else {cleaned.add(word);}
        }
        return cleaned;
    }
    public ArrayList<String> getWordList(String pathToStopFile, String pathToBookFile) {
        ArrayList<String> bookWords = new ArrayList<>();
        ArrayList<String> stopWords = readStopWords(pathToStopFile);
        bookWords = readBookWords(pathToBookFile);
        return cleanWords(bookWords, stopWords);
    }
}