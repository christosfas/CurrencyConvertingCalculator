package com.example.currencyconvertingcalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import android.util.Log;

public class Parser {
    private final List<ScannedToken> expression;
    private final String TAG = this.getClass().getSimpleName();

    public Parser(List<ScannedToken> expression) {
        this.expression = expression;
    }

    /*
    We need a triple in a sliding window fashion where middle is current token so we can contextualize what
    needs to be parsed into correct tokens
     */
    public List<ScannedToken> parse(){
        TokenType prev;
        TokenType curr;
        TokenType next;

        List<ScannedToken> properlyParsedExpression = new ArrayList<>();

        List<TokenType> types = expression.stream().map(ScannedToken::type).collect(Collectors.toList());
        List<Integer> indexes = new ArrayList<>();
        List<ScannedToken> negativeValues = new ArrayList<>();

        for (int i = 0; i < types.size() - 1; i++) {
            prev = i == 0 ? null : types.get(i - 1);
            curr = types.get(i);
            next = types.get(i + 1);
            if (prev == null && curr == TokenType.SUB && next == TokenType.VALUE) {
                ScannedToken negativeValue =
                        new ScannedToken("" + (-1 * Double.parseDouble(expression.get(i + 1).expression())),
                                TokenType.VALUE);
                Log.e(TAG, "new token at index " + i);
                indexes.add(i);
                negativeValues.add(negativeValue);

            } else if (prev == TokenType.LPAR && curr == TokenType.SUB && next == TokenType.VALUE) {
                ScannedToken negativeValue =
                        new ScannedToken("" + (-1 * Double.parseDouble(expression.get(i + 1).expression())),
                                TokenType.VALUE);
                Log.e(TAG, "new token at index " + i);
                indexes.add(i);
                negativeValues.add(negativeValue);
            }
        }

        int maxIterations = expression.size();
        int i = 0;
        int j = 0;
        while (i < maxIterations) {
            if (indexes.contains(i) && j < negativeValues.size()) {
                properlyParsedExpression.add(negativeValues.get(j));
                j++;
                i++;
            }
            else {
                properlyParsedExpression.add(expression.get(i));
            }
            i++;
        }
        Log.e(TAG, properlyParsedExpression.toString());
        return properlyParsedExpression;
    }
}
