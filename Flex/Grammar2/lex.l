/*Definition section*/
%{
    #undef yywrap
    #define yywrap() 1

    #define ERROR   -1
    #define PLUS    1
    #define MINUS   2
    #define MULT    3
    #define DIVIDE  4
    #define EQUAL   5
    #define GREATERTHAN 6
    #define LESSTHAN    7
    #define OPENPARA    8
    #define CLOSEPARA   9
    #define OPENCURLY   10
    #define CLOSECURLY  11
    #define ASSIGN      12
    #define SEMICOLON   13
    #define AND         14
    #define ELSE        15
    #define END         16
    #define INTCONST    17
    #define IDENTIFIER  18
    #define IF          19
    #define INT         20
    #define DO          21
    #define FLOATCONST  22
    #define FLOAT       23
    #define NOT         24
    #define OR          25
    #define PRINT       26
    #define PROG        27
    #define SCAN        28
    #define STR         29
    #define THEN        30
    #define WHILE       31

    /*Variable to keep the track of line number and index*/
    int lineNumber = 1;
    int indexInLine = 1;
%}

/*Tokens definition*/
plus    \+
minus   -
mult    \*
divide  \/
equal   =
greaterthan     \>
lessthan        \<
openpara        \(
closepara       \)
opencurly       \{
closecurly      \}
assign          :=
semicolon       ;
and             and
else            else
end             end

digit           [0-9]
letter          [a-zA-z]
intconst        {digit}{digit}*(E[+-]?{digit}{digit}*)?
floatconst      {digit}{digit}*\.{digit}{digit}*(E[+-]?{digit}{digit}*)?
identifier      {letter}({letter}|{digit})*
if              if
int             int
do              do
float           float
not             not
or              or
print           print
prog            prog
scan            scan
str             \"..*\"
then            then
while           while
delimiter       [ \t\n]

/*Rules section*/
%%
{and}       {
    indexInLine += yyleng;
    return AND;
}
{else}      {
    indexInLine += yyleng;
    return ELSE;
}
{end}       {
    indexInLine += yyleng;
    return END;
}
{if}        {
    indexInLine += yyleng;
    return IF;
}
{int}       {
    indexInLine += yyleng;
    return INT;
}
{do}        {
    indexInLine += yyleng;
    return DO;
}
{float}     {
    indexInLine += yyleng;
    return FLOAT;
}
{not}       {
    indexInLine += yyleng;
    return NOT;
}
{or}        {
    indexInLine += yyleng;
    return OR;
}
{print}     {
    indexInLine += yyleng;
    return PRINT;
}
{prog}      {
    indexInLine += yyleng;
    return PROG;
}
{scan}      {
    indexInLine += yyleng;
    return SCAN;
}
{then}      {
    indexInLine += yyleng;
    return THEN;
}
{while}     {
    indexInLine += yyleng;
    return WHILE;
}
{plus}      {
    indexInLine += yyleng;
    return PLUS;
}
{minus}     {
    indexInLine += yyleng;
    return MINUS;
}
{mult}      {
    indexInLine += yyleng;
    return MULT;
}
{divide}    {
    indexInLine += yyleng;
    return DIVIDE;
}
{equal}     {
    indexInLine += yyleng;
    return EQUAL;
}
{lessthan}  {
    indexInLine += yyleng;
    return LESSTHAN;
}
{greaterthan} {
    indexInLine += yyleng;
    return GREATERTHAN;
}
{openpara}  {
    indexInLine += yyleng;
    return OPENPARA;
}
{closepara} {
    indexInLine += yyleng;
    return CLOSEPARA;
}
{opencurly} {
    indexInLine += yyleng;
    return OPENCURLY;
}
{closecurly}    {
    indexInLine += yyleng;
    return CLOSECURLY;
}
{assign}    {
    indexInLine += yyleng;
    return ASSIGN;
}
{semicolon} {
    indexInLine += yyleng;
    return SEMICOLON;
}
{intconst}  {
    indexInLine += yyleng;
    return INTCONST;
}
{identifier}    {
    indexInLine += yyleng;
    return IDENTIFIER;
}
{floatconst} {
    indexInLine += yyleng;
    return FLOATCONST;
}
{str}       {
    indexInLine += yyleng;
    return STR;
}
{delimiter} {
    if (*yytext == '\n') {
        lineNumber++;
        indexInLine = 1;
    } else {
        indexInLine += yyleng;
    }
}
.           return ERROR;
%%

char* getNameFromToken(int token) {
    if (token == PLUS) {
        return "+";
    }
    if (token == MINUS) {
        return "-";
    }
    if (token == MULT) {
        return "*";
    }
    if (token == DIVIDE) {
        return "/";
    }
    if (token == EQUAL) {
        return "=";
    }
    if (token == LESSTHAN) {
        return "<";
    }
    if (token == GREATERTHAN) {
        return ">";
    }
    if (token == OPENPARA) {
        return "()";
    }
    if (token == CLOSEPARA) {
        return ")";
    }
    if (token == OPENCURLY) {
        return "}";
    }
    if (token == CLOSECURLY) {
        return "}";
    }
    if (token == ASSIGN) {
        return ":=";
    }
    if (token == SEMICOLON) {
        return ";";
    }
    if (token == AND) {
        return "and";
    }
    if (token == ELSE) {
        return "else";
    }
    if (token == END) {
        return "end";
    }
    if (token == INTCONST) {
        return "ic";
    }
    if (token == IDENTIFIER) {
        return "id";
    }
    if (token == IF) {
        return "if";
    }
    if (token == INT) {
        return "int";
    }
    if (token == DO) {
        return "do";
    }
    if (token == FLOATCONST) {
        return "fc";
    }
    if (token == FLOAT) {
        return "float";
    }
    if (token == NOT) {
        return "not";
    }
    if (token == OR) {
        return "or";
    }
    if (token == PRINT) {
        return "print";
    }
    if (token == PROG) {
        return "prog";
    }
    if (token == SCAN) {
        return "scan";
    }
    if (token == STR) {
        return "str";
    }
    if (token == THEN) {
        return "then";
    }
    if (token == WHILE) {
        return "while";
    }
    return "ERROR";
}

int main() {
    yyin = fopen("input.txt", "r");
    
    //remove the already existing output file
    remove("output.txt");

    int token;
    while((token = yylex()) != 0) {
        yyout = fopen("output.txt", "a");
        fprintf(yyout, "%s, %s, %d, %d\n", getNameFromToken(token), yytext, lineNumber, indexInLine);
        fclose(yyout);
    }

    return 0;
}