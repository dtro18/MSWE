package programming_styles.test_proj;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.json.XML;
public class XMLJsonAssignment {

    public JSONObject readXmlFile(String filename) {
        JSONObject jsonObj = new JSONObject();
        File initialFile = new File(filename);
        try {
            InputStream targetStream = new FileInputStream(initialFile);
            Reader targetReader = new InputStreamReader(targetStream);
            jsonObj = XML.toJSONObject(targetReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
    public void saveJsonObjAsJson(JSONObject jsonObj, String outputName) {
        // Print json object to its own json file.
        try (FileWriter writer = new FileWriter(outputName + ".json")) {
            writer.write(jsonObj.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void performTaskOne(String filename) {
        saveJsonObjAsJson(readXmlFile(filename), "taskOneOutput");
    }

    public void performTaskTwo() {
        // Presetting the xml file
        JSONObject mediumJsonObj = readXmlFile("books.xml");
        JSONPointer jsonPointer = new JSONPointer("/catalog");
        // Must cast to json object bc pointer returns just object
        saveJsonObjAsJson((JSONObject) jsonPointer.queryFrom(mediumJsonObj), "taskTwoOutput");
    }

    public void performTaskThree(String keypath) {
        JSONObject smallJsonObj = readXmlFile("books.xml");
        JSONPointer jsonPointer = new JSONPointer(keypath);
        if (jsonPointer.queryFrom(smallJsonObj) != null) {
            saveJsonObjAsJson(smallJsonObj, "taskThreeOutput");
        }
    }
    
    // Helper function for task 4 to recursively rename nested json objects.
    public JSONObject replacekeyInJSONObject(JSONObject jsonObject) {
        JSONObject jsonUpdated = new JSONObject();
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            String newKey = "swe262_" + key;
            // if (((jsonObject.get(key) instanceof String)||(jsonObject.get(key) instanceof Number)||jsonObject.get(key) ==null)) {
            if (value instanceof JSONObject) {
                jsonUpdated.put(newKey, replacekeyInJSONObject((JSONObject) value));
            } else if (value instanceof JSONArray) {
                jsonUpdated.put(newKey, replaceKeyInJsonArray((JSONArray) value));
            } else {
                jsonUpdated.put(newKey, value);
            }

        }
        return jsonUpdated;
    }

    // Helper function for task 4 to recursively rename json arrays.
    public JSONArray replaceKeyInJsonArray(JSONArray jsonArr) {
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < jsonArr.length(); i++) {
            Object elem = jsonArr.get(i);
            if (elem instanceof JSONObject) {
                newArray.put(replacekeyInJSONObject((JSONObject) elem));
            } else if (elem instanceof JSONArray) {
                newArray.put(replaceKeyInJsonArray((JSONArray) elem));
            }
        }
        return newArray;
    }
    
    public void performTaskFour() {
        JSONObject largeJsonObject = readXmlFile("books.xml");
        largeJsonObject = replacekeyInJSONObject(largeJsonObject);
        saveJsonObjAsJson(largeJsonObject, "taskFourOutput");
    }

    public void performTaskFive() {
        // Books has a list of books under the key "book".
        // Replacing with just one book, under the same key.
        JSONObject bookJsonObject = readXmlFile("books.xml");
        
        JSONObject book1 = new JSONObject();
        book1.put("author", "Tran, Dylan");
        book1.put("title", "Submission for Task 5");
        book1.put("genre", "Computer Science");
        book1.put("price", 99.99);
        book1.put("publish_date", "2025-04-11");
        book1.put("description", "Please give me an A!");

        JSONObject catalog =  bookJsonObject.getJSONObject("catalog");
        catalog.put("book", book1);

        saveJsonObjAsJson(bookJsonObject, "taskFiveOutput");
    }

    public static void main(String[] args) {
        // args[0] = xml file to read in first task
        // args[1] = key path to be checked in task 3
        String xmlFilePath = args[0];
        XMLJsonAssignment driver = new XMLJsonAssignment();

        // Task 1: Read xml file into json object form and write json back to disk.
        driver.performTaskOne(xmlFilePath);

        // Task 2: Read an XML file into a JSON object, and extract some smaller sub-object inside, given a certain path.
        driver.performTaskTwo();

        // Task 3: Read an XML file into a JSON object, check if it has a certain key path (given in the command line too). 
        // If so, save the JSON object to disk; if not, discard it.
        // Keypath to check: /feed/doc/title
        driver.performTaskThree(args[1]);

        
        // Task 4: Read an XML file into a JSON object, and add the prefix "swe262_" to all of its keys.
        driver.performTaskFour();
        
        // Task 5: Read an XML file into a JSON object, replace a sub-object on a certain key 
        // path with another JSON object that you construct, then write the result on disk as a JSON file. 
        driver.performTaskFive();
    }
}
