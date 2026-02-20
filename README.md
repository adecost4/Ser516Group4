# Cohesion Analyzer (LCOMHS)
This project computes LCOMHS (Henderson–Sellers cohesion) per Java class and output results with metadata (package, class, metric, timestamp).

## Execution of the Project
## Prerequisites
## Local Run
- Java 17
- Maven 3.9+
## Docker Run
- Docker Desktop (or Docker Engine)

## ----------------------------
## 1. Run with Docker Compose
docker compose up --build

## 2. Run with Docker - Build the image + Run
docker build -t lcomhs-metrics .
docker run --rm lcomhs-metrics

## 3. Run Locally (Maven)
mvn -q clean compile exec:java "-Dexec.mainClass=com.cohesion.Main" "-Dexec.args=src/main/java"


## Project Structure (important paths)
Source code: `src/main/java`
Sample files: `src/main/java/com/sample/...` (example: `FootballTeam.java`)
Main entry point: `com.cohesion.Main`

## Output:
Found 6 Java files:
src\main\java\com\cohesion\classes\MFResult.java
src\main\java\com\cohesion\classparsing\LCOMHSClassParser.java
src\main\java\com\cohesion\LCOMHSCalculator.java
src\main\java\com\cohesion\Main.java
src\main\java\com\cohesion\ProjectScanner.java
src\main\java\com\sample\football\FootballTeam.java
File: MFResult.java
Class: MFResult     
M (methods+ctors): 5
F (instance fields): 4
MF: 8
LCOMHS=0.750000
----------------------------------
File: LCOMHSClassParser.java
Class: LCOMHSClassParser
M (methods+ctors): 5
F (instance fields): 0
MF: 0
LCOMHS=0.000000
----------------------------------
File: LCOMHSCalculator.java
Class: LCOMHSCalculator
M (methods+ctors): 1
F (instance fields): 0
MF: 0
LCOMHS=0.000000
----------------------------------
File: Main.java
Class: Main
M (methods+ctors): 1
F (instance fields): 0
MF: 0
LCOMHS=0.000000
----------------------------------
File: ProjectScanner.java
Class: ProjectScanner
M (methods+ctors): 2
F (instance fields): 0
MF: 0
LCOMHS=0.000000
----------------------------------
File: FootballTeam.java
Class: FootballTeam
M (methods+ctors): 12
F (instance fields): 4
MF: 18
LCOMHS=0.681818
----------------------------------