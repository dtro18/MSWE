$ cd framework
$ javac *.java
$ jar cfm framework.jar manifest.mf *.class
$ cd ../app
$ javac -cp ../framework/framework.jar *.java
$ jar cf app.jar *.class
$ cd ../deploy
$ cp ../framework/*.jar ../app/*.jar .
Windows?
Copy-Item ../framework/*.jar -Destination .
Copy-Item ../app1/*.jar -Destination .

$ java -jar framework.jar 

cd week6_exercise
javac -d ./words1 ./words1/words1.java
javac -d ./words2 ./words2/words2.java
javac -d ./freq1 ./freq1/freq1.java
jar cf ./words1/words1.jar -C ./words1 .
jar cf ./words2/words2.jar -C ./words2 .
jar cf ./freq1/freq1.jar -C ./freq1 .