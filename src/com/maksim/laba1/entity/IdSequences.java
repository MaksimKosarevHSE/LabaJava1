package com.maksim.laba1.entity;

import java.io.Serial;
import java.io.Serializable;

public class IdSequences implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private int userId;
    private int accountId;
    private int transactionId;

    public IdSequences() {
        userId = 1;
        accountId = 1;
        transactionId = 1;
    }

    public int incrementUserId() {
        return userId++;
    }

    public int incrementAccountId() {
        return accountId++;
    }

    public int incrementTransactionId() {
        return transactionId++;
    }
}
