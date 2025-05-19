import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

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
            lastChar = Character.toString(Character.toLowerCase((char) character));
            return (character != -1 || word.length() > 0);
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

    public static void main(String[] args) {
        try {
            WordIterator wordIter = new WordIterator("test.txt");
            while (wordIter.hasNext()) {
                System.out.println(wordIter.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}