package week4_exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.nio.file.Paths;

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

        wfapp.register_for_load_event(data_load);

    }
    
    public void __load(String path_to_file) {

    }

    private class data_load implements Runnable {
        @Override
        public void run() {
            try {
                String content = Files.readString(Paths.get("stop_words.txt"));
                // Revisit this.
                _data = content;
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }

}

class StopWordFilter {

}

class load_event implements Runnable {
    @Override
    public void run() {

    }
}