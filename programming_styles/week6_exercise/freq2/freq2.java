import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class freq2 implements IFreq {

    static HashMap<Character, Integer> populateFreqDict(ArrayList<String> bookWords) {
        HashMap<Character, Integer> freqDict = new HashMap<>();
        for (String word : bookWords) {
            if (freqDict.containsKey(word.charAt(0))) {
                freqDict.put(word.charAt(0), freqDict.get(word.charAt(0)) + 1);
            } else {
                freqDict.put(word.charAt(0), 1);
            }
        }
        return freqDict;
    }
    static String printTop25(HashMap<Character, Integer> freqDict, Integer k) {
        PriorityQueue<Map.Entry<Character, Integer>> pq = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : freqDict.entrySet()) {pq.offer(entry);if (pq.size() > k) {pq.poll();}}
        while (!pq.isEmpty()) {Map.Entry<Character, Integer> entry = pq.poll();result.add(entry.getKey() + " - " + entry.getValue());}
        Collections.reverse(result);
        return String.join(" \n", result) + " \n";

    }

    public String getTop25(ArrayList<String> bookWords) {
        return printTop25(populateFreqDict(bookWords), 25);
    }
}
