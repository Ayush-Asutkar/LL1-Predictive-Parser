/*Definition section*/
%{
    #undef yywrap
    #define yywrap() 1

    #define ERROR -1
    #define N 1
    #define PLUS 2
    #define STAR 3

    /*Variable to keep the track of line number and index*/
    int lineNumber = 1;
    int indexInLine = 1;
%}

/*Tokens definition*/
n n
plus [+]
star [*]
delimiter [ \t\n]

/*Rules section*/
%%
{n}     {
    indexInLine += yyleng;
    return N;
}
{plus}  {
    indexInLine += yyleng;
    return PLUS;
}
{star}  {
    indexInLine += yyleng;
    return STAR;
}
{delimiter} {
    if (*yytext == '\n') {
        lineNumber++;
        indexInLine = 1;
    } else {
        indexInLine += yyleng;
    }
}
.       return ERROR;
%%

char* getNameFromToken(int token) {
    if (token == N) {
        return "n";
    }
    if (token == PLUS) {
        return "+";
    }
    if (token == STAR) {
        return "*";
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