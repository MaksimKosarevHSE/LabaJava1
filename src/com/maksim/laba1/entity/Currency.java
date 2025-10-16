package com.maksim.laba1.entity;

import java.io.Serial;
import java.io.Serializable;

public enum Currency implements Serializable {
    RUBLE("₽"), DOLLAR("$"), EURO("€");
    @Serial
    private static final long serialVersionUID = 1234567L;

    private final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

}
