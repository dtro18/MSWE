import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Map;
import java.util.Comparator;
import java.util.Scanner;

class WordFrequencyFramework {
    ArrayList<Runnable> _load_event_handlers = new ArrayList<>();
    ArrayList<Runnable> _dowork_event_handlers = new ArrayList<>();
    ArrayList<Runnable> _end_event_handlers = new ArrayList<>();

    public void register_for_load_event(Runnable handler) {
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

    // Define interface that defines behavior of lambda function
    @FunctionalInterface
    public interface WordEventHandler {
        void handle(String word);
    }

    String _data = "";
    StopWordFilter _stop_word_filter;
    ArrayList<WordEventHandler> _word_event_handlers = new ArrayList<>();

    public DataStorage(WordFrequencyFramework wfapp, StopWordFilter stop_word_filter) {
        _stop_word_filter = stop_word_filter;

        wfapp.register_for_load_event(
                () -> this.__load());

        wfapp.register_for_dowork_event(
                () -> this.__produce_words());

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
    }

    // Iterates thru list words in storage and calls handlers for each word
    public void __produce_words() {
        String[] words = _data.split(" ");
        for (String word : words) {
            if (_stop_word_filter.is_stop_word(word) | word.length() < 2) {
                continue;
            } else {
                for (WordEventHandler handler : _word_event_handlers) {
                    handler.handle(word);
                }
            }
        }
    }

    public void register_for_word_event(WordEventHandler handler) {
        _word_event_handlers.add(handler);
    }
}

class StopWordFilter {
    ArrayList<String> _stop_words = new ArrayList<>();

    public StopWordFilter(WordFrequencyFramework wfapp) {
        wfapp.register_for_load_event(
                () -> this.__load());
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
    }

    public boolean is_stop_word(String word) {
        return _stop_words.contains(word) ? true : false;
    }
}

class WordFrequencyCounter {
    HashMap<String, Integer> _word_freqs = new HashMap<>();

    public WordFrequencyCounter(WordFrequencyFramework wfapp, DataStorage data_storage) {
        data_storage.register_for_word_event(
                word -> this.__increment_count(word));
        wfapp.register_for_end_event(
                () -> this.__print_freqs());
    }

    // Should take a word...
    public void __increment_count(String word) {
        if (_word_freqs.containsKey(word)) {
            _word_freqs.put(word, _word_freqs.get(word) + 1);
        } else {
            _word_freqs.put(word, 1);
        }
    }

    public void __print_freqs() {
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : _word_freqs.entrySet()) {
            pq.offer(entry);
            if (pq.size() > 25) {
                pq.poll();
            }
        }
        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> entry = pq.poll();
            result.add(entry.getKey() + " - " + entry.getValue());
        }
        Collections.reverse(result);
        System.out.println(String.join(" \n", result) + " \n");
    }
}

public class Fifteen {
    public static void main(String[] args) {
        WordFrequencyFramework wfapp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        DataStorage dataStorageObj = new DataStorage(wfapp, stopWordFilter);
        WordFrequencyCounter freqCount = new WordFrequencyCounter(wfapp, dataStorageObj);
        wfapp.run();
    }
}