Setup:

1. Used Windows 10os, with Java SE8 Installed.
2. Extract Zip file into any directory. 
3. To run System_A, cd into System_A and execute the following commands:

javac *.java
java Plumber

- Make sure the data is in the directory of the system you're trying to run. So in this case, 
- FlightData.dat was located in the System_A directory.
4. Output will be created as an output.csv file in the System_A directory.
5. To run System_B, cd into System_B and execute the following commands:

javac *.java
java Plumber

- Again, make sure data is in the correct directory (System_B) this time.
6. Output will be created two files in the System_B directory:
outputB.csv
WildPoints.csv

- Make sure that if you want to try the system multiple times, you delete the csv files.
- System should generate new csv files.