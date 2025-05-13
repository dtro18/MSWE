# Commands to compile, package, and run. Copy/paste directly into terminal.

```
cd framework
javac *.java
jar cfm framework.jar manifest.mf *.class
cd ../words1
javac -cp ../framework/framework.jar *.java
jar cf words1.jar *.class
cd ../words2
javac -cp ../framework/framework.jar *.java
jar cf words2.jar *.class
cd ../freq1
javac -cp ../framework/framework.jar *.java
jar cf freq1.jar *.class
cd ../freq2
javac -cp ../framework/framework.jar *.java
jar cf freq2.jar *.class
cd ../framework
java -jar framework.jar

```

# To change the runtime functionality, navigate to config.properties and change parameters.
- There are four plugins total:
- words1, words2, freq1, freq2

