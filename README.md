# Compiler Design Lab

---

## Syntax Analysis/Parsing (LL(1) Predictive Parser)
## Following are my details for assignment submission:
<p>Name: &nbsp;&nbsp;&nbsp;&nbsp;Ayush Vinayak Asutkar</p>
<p>Roll No.: &nbsp;20CS01057</p>
<p>Semester: &nbsp;7th</p>
<p>Year of study: &nbsp;4th year</p>
<p>Subject: &nbsp;&nbsp;Compiler Design Laboratory</p>
<p>Assignment: &nbsp;Assignment - 5</p>

---

## How to run
1. Clone the repository: https://github.com/Ayush-Asutkar/LL1-Predictive-Parser.git
2. Open in your favourite editor. (The editor used while making this project was Intellij IDEA and also the path are currently coded to handle only windows)
3. Run the complete project by running the Main.java in src folder. Follow the prompt to give input.

## Problem Statement
For each of the grammars, perform the following steps and implement a predictive LL(1) parser.
1. Transform the given baseGrammar to LL(1).

    <details>
    <summary>Details:</summary>

    <p>Transform the baseGrammar to LL(1) (without affecting/changing the language) by (removing of left-recursing, and 
    making the baseGrammar left-factored. Write a LL(1) baseGrammar in a output file.</p>

    </details>
   
2. Compute First and Follow Set.
    <details>
    <summary>Details:</summary>
    
    <p>For the LL(1) baseGrammar obtains from step1, compute the FIRST set of all symbols in the baseGrammar, and also compute
    the FOLLOW set for all non-terminals. Store this information in another file.</p>
    
    </details>
   
3. Compute the parse table.
    <details>
    <summary>Details:</summary>
   
    <p>Create a parsing table for the LL(1) using FIRST and FOLLOW set.</p>    

    </details>
4. Take input a string and parse it using the parse table. Output "Accepted" and "Not Accepted" for the input string.

## Input Grammar Format
1. First line should contain the start symbol.
2. Second line should contain the List of Non-terminal symbols in space separated manner.
3. Next Line should contain the list of Terminal symbols in space separated manner.
4. Next till the end of file, should contain production rule in format:
   <p>P -> prog DL SL end | if else then</p>
   <p>The left hand side and right side should be separated by ->. The right hand side rules should be separated by |. And for each rule, the symbols should be space separated</p>

   <details>
   <summary>Example input baseGrammar</summary>

   <p>P</p>
   <p>AE BE D DL E F ES IOS IS NE P PE RE S SL T TY VL WS</p>
   <p>+ - * / = < > ( ) { } := ; and else end ic id if int do fc float not or print prog scan str then while</p>
   <p>P -> prog DL SL end</p>
   <p>DL -> D DL | ε</p>
   <p>D -> TY VL ;</p>
   <p>TY -> int | float</p>
   <p>VL -> id VL | id</p>
   <p>SL -> S SL | ε</p>
   <p>S -> ES | IS | WS | IOS</p>
   <p>ES -> id := E ;</p>
   <p>IS -> if BE then SL end | if BE then SL else SL end</p>
   <p>WS -> while BE do SL end</p>
   <p>IOS -> print PE | scan id</p>
   <p>PE -> E | str</p>
   <p>BE -> BE or AE | AE</p>
   <p>AE -> AE and NE | NE</p>
   <p>NE -> not NE | { BE } | RE</p>
   <p>RE -> E = E | E < E | E > E</p>
   <p>E -> E + T | E - T | T</p>
   <p>T -> T * F | T / F | F</p>
   <p>F -> ( E ) | id | ic | fc</p>

   </details>
   

<p>NOTE: The input baseGrammar should be written <a href="Input/InputGrammar.txt">here</a> and the input text <a href="Input/InputText.txt">here</a>.</p>

## Understanding the codebase
<p>The project consists of many folders, lets walk through one after another.</p>

1. Flex directory:
   
   <p>The flex directory consists of all the files related to flex. Currently, there are two subdirectories,
   Grammar1 and Grammar2. In each of this directory, there is a file with ".l" extension. This is the lex file, 
   which will be used for lexical analysis.</p>

2. Input directory:
   
   <p>This is the directory for input text file from which all the input is read. The files in this directory is, InputText.txt and 
   InputGrammar.txt. </p>

3. Output directory: (will be created on the go)
   <p>This directory contains the output files, namely "FirstFollowSet.txt", "EquivalentLeftFactoredandRemovingLeftRecursionGrammar.txt",
   "InputGrammar.txt", "ParsingSteps.txt", and "ParsingTable.txt". The names are quite self explanatory.</p>

4. src directory:
   <p>This directory contains all the source code. And the Main.java file which is the driver code for the complete program. Have a look at each of the subdirectory and the files in it. </p>


## Flow of working of the code
<p>Check the Main.java file to understand the flow. Following are the steps:</p>

1. Create empty output directory.
2. Take in the baseGrammar choice which needs to be run, currently there are two baseGrammar choices 1 and 2.
3. Read the text from InputText.txt and feed it the lex program.
4. Compile and run the flex program.
5. Take the input baseGrammar from "InputGrammar.txt".
6. Print the input baseGrammar to output file.
7. Make the baseGrammar as LL(1), apply algorithm for producing an equivalent left factored baseGrammar and algorithm for removal of left recursion, and print the baseGrammar to output file.
8. Compute the first and follow set, and print it to output file.
9. Read the list of tokens from flex program output file.
10. Create a parsing table for LL(1) baseGrammar, and print it to output file.
11. Feed the tokens to parser, which parses using parsing table.
12. Write the steps of parser to output file.
13. Print if the input text is accepted by the baseGrammar or not.


<p>For more details check the pdf: <a href="Question.pdf">"Question.pdf"</a></p>

## Future scope of this project
1. As the above written steps can be done in parallel manner, we can find a dependency graph for the above steps, and apply threading.
2. Currently, the program does not check if the baseGrammar is LL(1) or not. (This can be done by checking if their exists more than one production rule in a cell of parsing table.)
3. Also, the program does not check for error in lexical analysis explicitly. It will print error while parsing, but we can analyse the tokens and comment on lexical analysis.
4. The lex program is now hardcoded explicitly, we can write a program which can create a "lex.l" for us, and then use it for lexical analysis.
