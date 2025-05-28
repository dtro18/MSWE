import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ThirtyTwo {
    // Breaks string 
    public static ArrayList<ArrayList<String>> partition(Integer nLines) {
        ArrayList<ArrayList<String>> splits = new ArrayList<>();
        ArrayList<String> bookWords = new ArrayList<>();
        try {
            Scanner targetReader = new Scanner(new File("pride-and-prejudice.txt"));
            String line;
            // String[] words;
            Integer i = 0;
            
            while (targetReader.hasNextLine()) {
                if (i == nLines) {
                    splits.add(bookWords);
                    bookWords = new ArrayList<>();
                    i = 0;
                    continue;
                }
                line = targetReader.nextLine();
                if (line.length() == 0) {continue;}
                String[] words = line.split("[^a-zA-Z]+");
                for (String word : words) {
                    // System.out.println(word);
                    if (word.length() > 1) {
                        bookWords.add(word.toLowerCase());
                    }
                    
                    
                }
                i++;
            }
            if (bookWords.size() > 0) {
                splits.add(bookWords);
            }
            targetReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Target file not found");
        }
        return splits;

    }

    public static void main(String[] args) {
        ArrayList<ArrayList<String>> splits = partition(200);
        System.out.println(splits.size());
    }
}
