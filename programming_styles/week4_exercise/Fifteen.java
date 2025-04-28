import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.Scanner; 

class WordFrequencyFramework {
    ArrayList<Runnable> _load_event_handlers = new ArrayList<>();
    ArrayList<Runnable> _dowork_event_handlers = new ArrayList<>();
    ArrayList<Runnable> _end_event_handlers = new ArrayList<>();

    public void register_for_load_event (Runnable handler) {
        _load_event_handlers.add(handler);
    }

    public void register_for_dowork_event(Runnable handler) {
        _dowork_event_handlers.add(handler);
    }

    public void register_for_end_event(Runnable handler) {
        _end_event_handlers.add(handler);
    }

    public void run() {
        // Need path to file somehow...
        for (Runnable handler : _load_event_handlers) {
            handler.run();
        }

        for (Runnable handler : _dowork_event_handlers) {
            handler.run();
        }

        for (Runnable handler : _end_event_handlers) {
            handler.run();
        }
    }

}

class DataStorage {
    
    String _data = "";
    StopWordFilter _stop_word_filter;
    ArrayList<Runnable> _word_event_handlers = new ArrayList<>();

    public DataStorage(WordFrequencyFramework wfapp, StopWordFilter stop_word_filter) {
        _stop_word_filter = stop_word_filter;

        wfapp.register_for_load_event(
            () -> this.__load()
        );

    }
    
    public void __load() {
        String path_to_file = "pride-and-prejudice.txt";
        try {
            _data = Files.readString(Paths.get(path_to_file));
            Pattern pattern = Pattern.compile("[\\W_]+");
            _data = pattern.matcher(_data).replaceAll(" ").toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // System.out.println(_data);
        
    }
}

class StopWordFilter {
    ArrayList<String> _stop_words = new ArrayList<>();

    public StopWordFilter(WordFrequencyFramework wfapp) {
        wfapp.register_for_load_event(
            () -> this.__load()
        );
    }

    public void __load() {
        String path_to_file = "stop_words.txt";
        try {
            Scanner stopWordReader = new Scanner(new File(path_to_file));
            String str = stopWordReader.nextLine();
            String[] words = str.split(",");    
            for (int i = 0; i < words.length; i++) {
                _stop_words.add(words[i]);
            }
            stopWordReader.close();
        } catch (Exception e) {
            System.out.println("File not found");
        }   
        System.out.println(_stop_words);
    }
}

class load_event implements Runnable {
    @Override
    public void run() {

    }
}

public class Fifteen {
    public static void main(String[] args) {
        WordFrequencyFramework wfapp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        DataStorage dataStorageObj = new DataStorage(wfapp, stopWordFilter);
        wfapp.run();
    }
}