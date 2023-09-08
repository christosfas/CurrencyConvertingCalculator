package com.example.currencyconvertingcalculator;

public enum TokenType {
    ADD,
    SUB,
    MUL,
    DIV,
    POW,
    PERC,
    LPAR,
    RPAR,
    VALUE;

    @Override
    public String toString() {
        switch (this.ordinal()){
            case 0:
                return "+";
            case 1:
                return "-";
            case 2:
                return "*";
            case 3:
                return "/";
            case 4:
                return "^";
            case 5:
                return "%";
            case 6:
                return "(";
            case 7:
                return ")";
            case 8:
                return this.name();
            default:
                return "null";
        }
    }

    public static TokenType fromString(String s){
        switch (s){
            case "+":
                return TokenType.ADD;
            case "-":
                return TokenType.SUB;
            case "*":
                return TokenType.MUL;
            case "/":
                return TokenType.DIV;
            case "^":
                return TokenType.POW;
            case "%":
                return TokenType.PERC;
            case "(":
                return TokenType.LPAR;
            case ")":
                return TokenType.RPAR;
            default:
                return TokenType.VALUE;
        }
    }
}
