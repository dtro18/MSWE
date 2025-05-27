import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Pattern;


import java.util.regex.Matcher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

class Message {

    ArrayList<String> argList;
    ActiveWFObject activeWFObject;

    Message(ArrayList<String> argList, ActiveWFObject activeWFObject) {
        this.argList = argList;
        this.activeWFObject = activeWFObject;
    }

    Message(ArrayList<String> argList) {
        this.argList = argList;
        this.activeWFObject = null;
    }
}

abstract class ActiveWFObject implements Runnable {
    String name;
    // Queue will be a series of key, value pairs 
    ConcurrentLinkedQueue<Message> messageQueue;
    Boolean stopMe;

    public ActiveWFObject(String name) {
        this.name = name;
        messageQueue = new ConcurrentLinkedQueue<>();
        stopMe = false;
        new Thread(this).start();
    }

    public static void send(ActiveWFObject receiver, Message message) {
        receiver.messageQueue.add(message);
    }

    public void run() {
        while (this.stopMe == false) {
            Message message = messageQueue.poll();
            if (message != null) {
                _dispatch(message);
                if (message.argList.get(0).equals("die")) {
                    this.stopMe = true;
                }
            }
        }
    }

    abstract void _dispatch(Message message);

}

class DataStorageManager extends ActiveWFObject {
    String _data;
    StopWordManager stopWordManager;

    DataStorageManager() {
        super("dataStorageManager");
        _data = "";
    }

    void _dispatch(Message message) {
        String command = message.argList.get(0);
        if (command.equals("init")) {
            _init(message.argList.subList(1, message.argList.size()), message.activeWFObject);
        } else if (command.equals("sendWordFreqs")) {
            // System.out.println("StorageManager received sendWordFreqs command.");
            ArrayList<String> argList = new ArrayList<String>();
            argList.addAll(message.argList.subList(1, message.argList.size()));
            _process_words(new Message(argList, message.activeWFObject));
            // System.out.println("Storage dispatch called, process words called. Passing in WFC");
        } else {
            send(this.stopWordManager, message);
        }
    }

    void _init(List<String> initArgs, ActiveWFObject activeWFObject) {
        String pathToFile = "pride-and-prejudice.txt";
        if (activeWFObject instanceof StopWordManager) {
            this.stopWordManager = (StopWordManager) activeWFObject;
        } else {
            this.stopWordManager = null;
        }

        try {
            String data = Files.readString(Paths.get(pathToFile));
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(data);
            this._data = matcher.replaceAll(" ").toLowerCase();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    // Is sent a wordfrequencycontroller
    void _process_words(Message message) {
        WordFrequencyController recipient = (WordFrequencyController) message.activeWFObject;
        String[] words = _data.split(" ");
        for (String word : words) {
            ArrayList<String> argList = new ArrayList<String>();
            argList.add("filter");
            argList.add(word);

            send(this.stopWordManager, new Message(argList));
        }
        ArrayList<String> argList = new ArrayList<String>();
        argList.add("top25");
        send(this.stopWordManager, new Message(argList, recipient));

    }
}

class StopWordManager extends ActiveWFObject {
    ArrayList<String> _stopWords;
    WordFrequencyManager _wordFrequencyManager;
    StopWordManager() {
        super("stopWordManager");
        _stopWords = new ArrayList<>();
    }

    void _dispatch(Message message) {
        String command = message.argList.get(0);
        if (command.equals("init")) {
            ArrayList<String> argList = new ArrayList<String>();
            argList.addAll(message.argList.subList(1, message.argList.size()));
            this._init(new Message(argList, message.activeWFObject));
        } else if (command.equals("filter")) {
            ArrayList<String> argList = new ArrayList<String>();
            argList.addAll(message.argList.subList(1, message.argList.size()));
            // Do we need to return anything here??
            this._filter(new Message(argList));
        } else {
            // System.out.println(message.argList.size());
            // for (String str : message.argList) {
            //     System.out.println(str);
            // }
            send(this._wordFrequencyManager, message);
        }
    }

    void _init(Message message) {
        ArrayList<String> stopWords = new ArrayList<>();
        try {
            Scanner targetReader = new Scanner(new File("stop_words.txt"));
            String line;
            String[] words;
            while (targetReader.hasNextLine()) {
                line = targetReader.nextLine();
                if (line.length() == 0) {continue;}
                words = line.split(",");
                for (String word : words) {stopWords.add(word);}
            }
            targetReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Target file not found");
        }
        _stopWords = stopWords;

        this._wordFrequencyManager = (WordFrequencyManager) message.activeWFObject;
    }

    void _filter(Message message) {
        String word = message.argList.get(0);
        if (!_stopWords.contains(word) && word.length() > 1) {
            ArrayList<String> argList = new ArrayList<String>();
            argList.add("word");
            argList.add(word);
            send(this._wordFrequencyManager, new Message(argList));
        }
        
    }
}
class WordFrequencyManager extends ActiveWFObject {
    HashMap<String, Integer> _wordFreqs;

    WordFrequencyManager() {
        super("wordFrequencyManager");
        _wordFreqs = new HashMap<>();
        // System.out.println("WFManager called");
    }

    void _dispatch(Message message) {
        // System.out.println(message.argList.size());
        for (String str : message.argList) {
            // System.out.println(str);
        }
        String command = message.argList.get(0);
        ArrayList<String> trimmedArgList = new ArrayList<String>();
        trimmedArgList.addAll(message.argList.subList(1, message.argList.size()));

        if (command.equals("word")) {
            this._incrementCount(new Message(trimmedArgList));
        } else if (command.equals("top25")) {
            this._top25(new Message(trimmedArgList, message.activeWFObject));
        }
    }

    void _incrementCount(Message message) {
        String word = message.argList.get(0);
        if (this._wordFreqs.containsKey(word)) {
        this._wordFreqs.put(word, this._wordFreqs.get(word) + 1);
        } else {
            this._wordFreqs.put(word, 1);
        }
    }

    void _top25(Message message) {
        ActiveWFObject recipient = message.activeWFObject;

        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : this._wordFreqs.entrySet()) {pq.offer(entry);if (pq.size() > 25) {pq.poll();}}
        while (!pq.isEmpty()) {Map.Entry<String, Integer> entry = pq.poll();result.add(entry.getKey() + " - " + entry.getValue());}
        Collections.reverse(result);

        ArrayList<String> argList = new ArrayList<String>();
        argList.add("top25");
        argList.add((String.join(" \n", result) + " \n"));
        send(recipient, new Message(argList));
    }
}

class WordFrequencyController extends ActiveWFObject {
    ActiveWFObject _storageManager;

    WordFrequencyController() {
        super("wordFrequencyController");
        // System.out.println("FrequencyController constructor called");
    }
    void _dispatch(Message message) {
        // System.out.println("WordFrequencyController dispatch called");
        String command = message.argList.get(0);
        ArrayList<String> trimmedArgList = new ArrayList<String>();
        trimmedArgList.addAll(message.argList.subList(1, message.argList.size()));

        if (command.equals("run")) {
            this._run(new Message(trimmedArgList, message.activeWFObject));
        } else if (command.equals("top25")) {
            this._display(new Message(trimmedArgList, message.activeWFObject));
        } else {
            // System.out.println("Invalid message");
        }
    }

    void _run(Message message) {
        // System.out.println("Setting storage manager of WFC");
        this._storageManager = message.activeWFObject;

        if (this._storageManager == null) {
            System.out.println("Error: Storage manager is null!");
            return;
        }
        // System.out.println("Storage manager successfully set.");
        ArrayList<String> argList = new ArrayList<String>();
        argList.add("sendWordFreqs");
        send(this._storageManager, new Message(argList, this));
        // System.out.println("Sending sendWordFreqs command and WFC to storage manager success.");
    }   

    void _display(Message message) {
        System.out.println(message.argList.get(0));
        // CHANGE 4: Add null check here too to prevent crashes
        if (this._storageManager != null) {
            ArrayList<String> argList = new ArrayList<String>();
            argList.add("die");
            send(this._storageManager, new Message(argList));
        }
        this.stopMe = true;
    }
}


public class TwentyNine {
    public static void main(String[] args) {
        // System.out.println("Programstart.");
        // Create a new wordFrequencyManager.
        WordFrequencyManager wordFreqManager = new WordFrequencyManager();
        // System.out.println("wordFreqManager created.");
        StopWordManager stopWordManager =  new StopWordManager();
        // System.out.println("Stopwordmanager created.");

        ArrayList<String> argList1 = new ArrayList<String>();
        argList1.add("init");
        ActiveWFObject.send(stopWordManager, new Message(argList1, wordFreqManager));

        DataStorageManager storageManager =  new DataStorageManager();
        ArrayList<String> argList2 = new ArrayList<String>();
        argList2.add("init");
        ActiveWFObject.send(storageManager, new Message(argList2, stopWordManager));

        WordFrequencyController wfController = new WordFrequencyController();
        ArrayList<String> argList3 = new ArrayList<String>();
        argList3.add("run");
        ActiveWFObject.send(wfController, new Message(argList3, storageManager));
    }
    
}
