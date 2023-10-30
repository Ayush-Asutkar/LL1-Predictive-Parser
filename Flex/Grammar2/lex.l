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
    int indexInLine = 0;
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
    return AND;
}
{else}      {
    return ELSE;
}
{end}       {
    return END;
}
{if}        {
    return IF;
}
{int}       {
    return INT;
}
{do}        {
    return DO;
}
{float}     {
    return FLOAT;
}
{not}       {
    return NOT;
}
{or}        {
    return OR;
}
{print}     {
    return PRINT;
}
{prog}      {
    return PROG;
}
{scan}      {
    return SCAN;
}
{then}      {
    return THEN;
}
{while}     {
    return WHILE;
}
{plus}      {
    return PLUS;
}
{minus}     {
    return MINUS;
}
{mult}      {
    return MULT;
}
{divide}    {
    return DIVIDE;
}
{equal}     {
    return EQUAL;
}
{lessthan}  {
    return LESSTHAN;
}
{greaterthan} {
    return GREATERTHAN;
}
{openpara}  {
    return OPENPARA;
}
{closepara} {
    return CLOSEPARA;
}
{opencurly} {
    return OPENCURLY;
}
{closecurly}    {
    return CLOSECURLY;
}
{assign}    {
    return ASSIGN;
}
{semicolon} {
    return SEMICOLON;
}
{intconst}  {
    return INTCONST;
}
{identifier}    {
    return IDENTIFIER;
}
{floatconst} {
    return FLOATCONST;
}
{str}       {
    return STR;
}
{delimiter} {
    if (*yytext == '\n') {
        lineNumber++;
        indexInLine = 0;
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
        indexInLine += yyleng;
        fclose(yyout);
    }

    return 0;
}