REM first argument is: 0, 1, 2
javac -cp "gson-2.8.2.jar" BlockInputG.java
java -cp ".;gson-2.8.2.jar" BlockInputG %1
