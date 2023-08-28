package model;

public class Token {
    private final String tokenName;
    private final String value;

    public Token(String tokenName, String value) {
        this.tokenName = tokenName;
        this.value = value;
    }

    public String getTokenName() {
        return tokenName;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenName='" + tokenName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
