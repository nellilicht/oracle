# Technical assignment

## Introduction ##

Program is doing the following:
1. It takes an input file and parses the file, resulting in a list of alphabetically labelled candidates; (min 2 candidates, max 25 candidates)
2. List of the candidates is read from resources/candidates.txt
3. All options are displayed to the user and user input is requested (one or more times);
  3.1 User has to insert the lables of their preference;
4. User input is parsed and data is saved;
  4.1 Ballots are sorted and stored according to their first candidate preference;
5. By the input of word "Tally" - vote calculation is initiated;
6. Votes are calculated by the system described in the specification;
  6.1 If all votes divide equally, random candidate is eliminated;
7. Winner candidate is displayed to the user;

## Code and style ##

My goal was to use Java 8 possibilites. I chose collections by their features (e.g keeping an order), because I used maps and lists for storing user input and calculating the winner candidate. I created small methods with minimal responsibilities and kept naming as descriptive as possible.

## Architecture ##

I chose git for versioning, Maven to resolve dependencies, test and run program, Mockito for testing, Jacoco for test results. My goal was to divide the program logic into small logical pieces and keep the responsibilites clear. I tried to extract the dependencies so that I could test without them. (e.g Scanner). 

## Running the program (from Command line) ##

Navigate to folder, where pom.xml is located

    * **mvn compile**
    * **(optional) mvn test**
    * **mvn exec:java -Dexec.mainClass="Main"**

_NB! Test reprort can be found in target/site/jacoco/index.html (requires mvn test to be run first)_
