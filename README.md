![winedy-logo](src/main/resources/images/winedy_logo.png)

*<div align="center">A desktop app to help amateur and professional someliers catalogue, select and manage their wines.</div>*

## Authors
- SENG202 Teaching team
- Sophia Copley
- Rafe Dunlop
- Hannah Botting
- Steven Leishman
- Navaneethakrishna Sridhar
- Yuvraj Singh Fagotra

## Prerequisites
- JDK >= 21 [click here to get the latest stable OpenJDK release (as of writing this README)](https://jdk.java.net/18/)
- Gradle [Download](https://gradle.org/releases/) and [Install](https://gradle.org/install/)


## What's Included
This project comes with some basic examples of the following (including dependencies in the build.gradle file):
- JavaFX
- Logging (with Log4J)
- Junit 5
- Mockito (mocking unit tests)
- Cucumber (for acceptance testing)

We have also included a basic setup of the Gradle project and Tasks required for the course including:
- Required dependencies for the functionality above
- Build plugins:
    - JavaFX Gradle plugin for working with (and packaging) JavaFX applications easily

The app only requires the jar file to run. When the jar file is run, the database is created automatically in the same location as the jar file. If the user moves the jar file to a different location, the app will recognise that the database no longer exists and create a new database in the new location.

## Importing Project (Using IntelliJ)
IntelliJ has built-in support for Gradle. To import your project:

- Launch IntelliJ and choose `Open` from the start up window.
- Select the project and click open
- At this point in the bottom right notifications you may be prompted to 'load gradle scripts', If so, click load

**Note:** *If you run into dependency issues when running the app or the Gradle pop up doesn't appear then open the Gradle sidebar and click the Refresh icon.*

## Build Project 
1. Open a command line interface inside the project directory and run `./gradlew build` to build a .jar file. The file is located at target/Winedy-1.0-SNAPSHOT.jar

## Run App
- Open a command line interface inside the project directory and run `cd target` to change into the target directory.
- Run the command `java -jar Winedy-1.0-SNAPSHOT.jar` to open the application.
