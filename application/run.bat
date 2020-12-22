set CLASSPATH=%CLASSPATH%;libs\JDiagram.jar
md classes
javac -d classes src/*.java
cd classes
java -cp ./;..\libs\JDiagram.jar;..\libs\mysql-connector-java-5.1.40-bin.jar MainWindow
cd..
pause..