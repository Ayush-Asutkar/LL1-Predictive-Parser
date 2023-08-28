/*Definition section*/
%{
    #undef yywrap
    #define yywrap() 1

    #define ERROR -1
    #define N 1
    #define PLUS 2
    #define STAR 3
%}

/*Tokens definition*/
n n
plus [+]
star [*]

/*Rules section*/
%%
{n}     return N;
{plus}  return PLUS;
{star}  return STAR;
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
        fprintf(yyout, "(%s, %s)\n", getNameFromToken(token), yytext);
        fclose(yyout);
    }

    return 0;
}