To compile:
javac -cp "lib/json-20250107.jar" -d out XMLJsonAssignment.java

To run script with two args, file to be read in task 1 and path to be searched in task 3.
java -cp "lib/json-20250107.jar;out" programming_styles.test_proj.XMLJsonAssignment books.xml /catalog


Output when a huge (2gb) file was run:
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at java.base/java.lang.AbstractStringBuilder.<init>(AbstractStringBuilder.java:88)
        at java.base/java.lang.StringBuilder.<init>(StringBuilder.java:106)
        at org.json.XMLTokener.nextToken(XMLTokener.java:309)
        at org.json.XML.parse(XML.java:267)
        at org.json.XML.parse(XML.java:421)
        at org.json.XML.parse(XML.java:421)
        at org.json.XML.parse(XML.java:421)
        at org.json.XML.toJSONObject(XML.java:717)
        at org.json.XML.toJSONObject(XML.java:660)
        at programming_styles.test_proj.XMLJsonAssignment.readXmlFile(XMLJsonAssignment.java:26)
        at programming_styles.test_proj.XMLJsonAssignment.performTaskOne(XMLJsonAssignment.java:42)
        at programming_styles.test_proj.XMLJsonAssignment.main(XMLJsonAssignment.java:130)