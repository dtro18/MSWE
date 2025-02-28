To compile:
1. cd into lab3 (or the base directory that contains the source code)
2. execute javac *.java
4. There are three programs that must be running concurrently. Run them with the following commands, 
   making sure that all created terminals are in the correct directory (Lab3):

Start database first:
java Database.java Students.txt Courses.txt

Then, start the server:
java Server.java

Lastly, start the client:
java Client.java


logs.txt will be generated upon running the program.