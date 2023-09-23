package com.example.currencyconvertingcalculator;

import androidx.annotation.NonNull;

public class ScannedToken {
    private final String expressionPiece;
    private final TokenType type;

    public ScannedToken(String exp, TokenType type){
        this.expressionPiece = exp;
        this.type = type;
    }

    @NonNull
    @Override
    public String toString(){
        return "(Expr:"+ expressionPiece+ ", Token:"+ type+")";
    }

    public TokenType type(){
        return type;
    }

    public String expression(){
        return expressionPiece;
    }
}
