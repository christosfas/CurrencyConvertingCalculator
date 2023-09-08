package com.example.currencyconvertingcalculator;

import java.util.Map;

public class SymbolsResponse {   //a class describing the Fixer.io symbols endpoint response
    private boolean success;
    private Map<String, String> symbols;

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, String> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, String> symbols) {
        this.symbols = symbols;
    }
}
