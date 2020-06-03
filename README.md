# Java source code lines counter
This small command-line application is a simplistic *time-framed* take on implementing [Java source code counter kata](http://codekata.com/kata/kata13-counting-code-lines/), with few additional requirements relating to visualizing output of the program. Application counts java source code lines for each of the files in provided directory, omitting any comments in source code. Its *true* purpose, however, is just to show how I usually write and structure the code; this is not the final or perfect solution.

#Usage
* Build application via `gradle build`
* Run generated jar file from build/libs: `java -jar build/libs/0.0.1-source-line-counter.jar`
* When prompted, provide name of directory which should be scanned for java source code. Application accepts both absolute and relative paths; for example, to scan source code lines of the application itself, just provide `.` as an input.
