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
2. Open in your favourite editor. (The editor used while making this project was Intellij IDEA and also the path are currently hardcoded to handle only windows)
3. Run the complete project by running the Main.java in src folder. Follow the prompt to give input.

## Problem Statement
For each of the grammars, perform the following steps and implement a predictive LL(1) parser.
1. Transform the given grammar to LL(1).
2. Compute First and Follow Set.
3. Compute the parse table.
4. Take input a string and parse it using the parse table. Output "Accepted" and "Not Accepted", in their sense.

## Input Grammar Format
1. First line should contain the start symbol.
2. Second line should contain the List of Non-terminal symbols in space separated manner.
3. Next Line should contain the list of Terminal symbols in space separated manner.
4. Next till the end of file, should contain production rule in format:
<p>P -> prog DL SL end | if else then</p>
<p>The left hand side and right side should be separated by ->. The right hand side rules should be separated by |. And for each rule, the symbols should be space separated</p>
<p>For more details check the pdf: <a href="Question.pdf">"Question.pdf"</a></p>
