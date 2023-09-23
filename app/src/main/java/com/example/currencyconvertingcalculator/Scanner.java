package com.example.currencyconvertingcalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import android.util.Log;

public class Scanner {
    private final String expression;
    private final String TAG = this.getClass().getSimpleName();

    public Scanner(String expr) {
        this.expression = expr;
    }

    public List<ScannedToken> scan() {   //from String expression to tokens
        StringBuilder value = new StringBuilder();
        List<ScannedToken> scannedExpr = new ArrayList<>();
        for (char c : expression.toCharArray()) {
            TokenType type = TokenType.fromString(String.valueOf(c));
            if (!type.equals(TokenType.VALUE)) {
                if (value.length() > 0) {
                    //Add the full value TOKEN
                    ScannedToken st = new ScannedToken(value.toString(), TokenType.VALUE);
                    scannedExpr.add(st);
                }
                value = new StringBuilder(String.valueOf(c));
                ScannedToken st = new ScannedToken(value.toString(), type);
                scannedExpr.add(st);
                value = new StringBuilder();
            } else {
                value.append(c);

            }
        }
        if (value.length() > 0) {
            //Add the full value TOKEN
            ScannedToken st = new ScannedToken(value.toString(), TokenType.VALUE);
            scannedExpr.add(st);
        }

        return scannedExpr;
    }

    public double evaluate(List<ScannedToken> tokenizedExpression) throws Exception{  //evaluate a tokenized expression

        if (tokenizedExpression.size() == 1) {
            return Double.parseDouble(tokenizedExpression.get(0).expression());
        }
        //Eval order is P.E.M.D.A.S. - Parenthesis, exponents, multiply, divide, add, subtract
        List<ScannedToken> simpleExpr = new ArrayList<>();

        int idx =
                tokenizedExpression.stream()
                        .map(ScannedToken::type)
                        .collect(Collectors.toList())
                        .lastIndexOf(TokenType.LPAR);
        int matchingRPAR = -1;
        if (idx >= 0) {
            for (int i = idx + 1; i < tokenizedExpression.size(); i++) {
                ScannedToken curr = tokenizedExpression.get(i);
                if (curr.type() == TokenType.RPAR) {
                    matchingRPAR = i;
                    break;
                } else {
                    simpleExpr.add(tokenizedExpression.get(i));
                }
            }
            if(matchingRPAR == -1) throw new Exception("Parenthesis never closed");
        } else {
            simpleExpr.addAll(tokenizedExpression);
            return evaluateSimpleExpression(tokenizedExpression);
        }

        double value = evaluateSimpleExpression(simpleExpr);
        //   Log.e(TAG,"val is " + value);
        List<ScannedToken> partiallyEvaluatedExpression = new ArrayList<>();
        for (int i = 0; i < idx; i++) {
            partiallyEvaluatedExpression.add(tokenizedExpression.get(i));
        }
        partiallyEvaluatedExpression.add(new ScannedToken(Double.toString(value), TokenType.VALUE));
        for (int i = matchingRPAR + 1; i < tokenizedExpression.size(); i++) {
            partiallyEvaluatedExpression.add(tokenizedExpression.get(i));
        }

        // from idx find first ), extract, evaluate, replace, call recursively
        //  Log.e(TAG,"Expr to eval indexes: " + idx + ", " + matchingRPAR);
        Log.e(TAG, partiallyEvaluatedExpression.toString());
        return evaluate(partiallyEvaluatedExpression);
    }

    //A simple expression won't contain parenthesis
    public double evaluateSimpleExpression(List<ScannedToken> expression) throws Exception{
        if (expression.size() == 1) {
            return Double.parseDouble(expression.get(0).expression());
        } else {
            List<ScannedToken> newExpression = new ArrayList<>();
            int idx = expression.stream().map(ScannedToken::type).collect(Collectors.toList()).indexOf(TokenType.POW);
            if (idx != -1) {
                if(!(expression.size()>idx+1)||idx==0){
                    throw new Exception("Power operator missing operands");
                }
                double base = Double.parseDouble(expression.get(idx - 1).expression());
                double exp = Double.parseDouble(expression.get(idx + 1).expression());
                double ans = Math.pow(base, exp);
                for (int i = 0; i < idx - 1; i++) {
                    newExpression.add(expression.get(i));
                }
                newExpression.add(new ScannedToken(ans + "", TokenType.VALUE));
                for (int i = idx + 2; i < expression.size(); i++) {
                    newExpression.add(expression.get(i));
                }
                return evaluateSimpleExpression(newExpression);
            } else {
                int percIdx = expression.stream()
                        .map(ScannedToken::type)
                        .collect(Collectors.toList())
                        .indexOf(TokenType.PERC);
                if (percIdx != -1) {
                    if(percIdx==0){
                        throw new Exception("Percentage operator missing operand");
                    }
                    double left = Double.parseDouble(expression.get(percIdx - 1).expression());
                    double ans = left * 0.01;
                    for (int i = 0; i < percIdx - 1; i++) {
                        newExpression.add(expression.get(i));
                    }
                    newExpression.add(new ScannedToken(ans + "", TokenType.VALUE));
                    for (int i = percIdx + 1; i < expression.size(); i++) {
                        newExpression.add(expression.get(i));
                    }
                    return evaluateSimpleExpression(newExpression);

                } else {
                    int mulIdx = expression.stream()
                            .map(ScannedToken::type)
                            .collect(Collectors.toList())
                            .indexOf(TokenType.MUL);
                    int divIdx = expression.stream()
                            .map(ScannedToken::type)
                            .collect(Collectors.toList())
                            .indexOf(TokenType.DIV);
                    int computationIdx = (mulIdx >= 0 && divIdx >= 0) ? Math.min(mulIdx, divIdx) : Math.max(mulIdx, divIdx);
                    if (computationIdx != -1) {
                        if(!(expression.size()>computationIdx+1)||computationIdx==0){
                            throw new Exception("Multiplication/Division operator missing operands");
                        }
                        double left = Double.parseDouble(expression.get(computationIdx - 1).expression());
                        double right = Double.parseDouble(expression.get(computationIdx + 1).expression());
                        double ans = computationIdx == mulIdx ? left * right : left / right;
                        for (int i = 0; i < computationIdx - 1; i++) {
                            newExpression.add(expression.get(i));
                        }
                        newExpression.add(new ScannedToken(ans + "", TokenType.VALUE));
                        for (int i = computationIdx + 2; i < expression.size(); i++) {
                            newExpression.add(expression.get(i));
                        }
                        return evaluateSimpleExpression(newExpression);
                    } else {
                        int addIdx = expression.stream()
                                .map(e -> e.type())
                                .collect(Collectors.toList())
                                .indexOf(TokenType.ADD);
                        int subIdx = expression.stream()
                                .map(e -> e.type())
                                .collect(Collectors.toList())
                                .indexOf(TokenType.SUB);
                        int computationIdx2 = (addIdx >= 0 && subIdx >= 0) ?
                                Math.min(addIdx, subIdx) :
                                Math.max(addIdx, subIdx);
                        if (computationIdx2 != -1) {
                            if(!(expression.size()>computationIdx2+1)){
                                throw new Exception("Add/Sub operator missing operand");
                            }
                            double left = Double.parseDouble(expression.get(computationIdx2 - 1).expression());
                            double right = Double.parseDouble(expression.get(computationIdx2 + 1).expression());
                            double ans = computationIdx2 == addIdx ? left + right : (left - right);
                            for (int i = 0; i < computationIdx2 - 1; i++) {
                                newExpression.add(expression.get(i));
                            }
                            newExpression.add(new ScannedToken(ans + "", TokenType.VALUE));
                            for (int i = computationIdx2 + 2; i < expression.size(); i++) {
                                newExpression.add(expression.get(i));
                            }
                            return evaluateSimpleExpression(newExpression);
                        }
                    }

                }
            }
            throw new Exception("Invalid expression");
        }
    }
}
