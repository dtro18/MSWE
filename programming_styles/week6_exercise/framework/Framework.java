import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class main {
    public static void main(String [] args) {
        try {
            Properties props = new Properties();
            String wordsJarPath = null;
            String freqJarPath = null;
            String wordClassName = null;
            String freqClassName = null;
            try (FileInputStream fis = new FileInputStream("config.properties")) {
                props.load(fis);
                wordsJarPath = props.getProperty("WORD_LOADER_JAR_PATH");
                freqJarPath = props.getProperty("FREQ_LOADER_JAR_PATH");
                wordClassName = props.getProperty("WORD_CLASS_NAME");
                freqClassName = props.getProperty("FREQ_CLASS_NAME");

            } catch (IOException e) {
                System.out.println("Config file not found");
                e.printStackTrace();
            }
            // Load the plugin JARs
            URL[] classUrls = {
                new File(wordsJarPath).toURI().toURL(),
                new File(freqJarPath).toURI().toURL(),
            };
            URLClassLoader cloader = new URLClassLoader(classUrls);
            // Load class 
            Class<?> cls1 = cloader.loadClass(wordClassName); 
            Class<?> cls2 = cloader.loadClass(freqClassName);
            // Create instance
            IWords wordPlugin = (IWords) cls1.getDeclaredConstructor().newInstance();
            IFreq freqPlugin = (IFreq) cls2.getDeclaredConstructor().newInstance();
            // Use the plugin
            ArrayList<String> words = wordPlugin.getWordList("stop_words.txt", "pride-and-prejudice.txt");
            String top25 = freqPlugin.getTop25(words);
            System.out.println(top25);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
