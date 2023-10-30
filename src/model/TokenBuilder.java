package model;

public class TokenBuilder {
    private String tokenName;
    private String value;
    private int lineNumber;
    private int indexInLine;

    public TokenBuilder setTokenName(String tokenName) {
        this.tokenName = tokenName;
        return this;
    }

    public TokenBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public TokenBuilder setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public TokenBuilder setIndexInLine(int indexInLine) {
        this.indexInLine = indexInLine;
        return this;
    }

    public Token buildToken() {
        return new Token(this.tokenName, this.value, this.lineNumber, this.indexInLine);
    }
}
