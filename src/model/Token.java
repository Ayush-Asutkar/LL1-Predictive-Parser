package model;

public class Token {
    private final String tokenName;
    private final String value;
    private final int lineNumber;
    private final int indexInLine;

    public Token(String tokenName, String value, int lineNumber, int indexInLine) {
        this.tokenName = tokenName;
        this.value = value;
        this.lineNumber = lineNumber;
        this.indexInLine = indexInLine;
    }

    public String getTokenName() {
        return tokenName;
    }

    public String getValue() {
        return value;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getIndexInLine() {
        return indexInLine;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenName='" + tokenName + '\'' +
                ", value='" + value + '\'' +
                ", lineNumber=" + lineNumber +
                ", indexInLine=" + indexInLine +
                '}';
    }
}
