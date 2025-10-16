package com.maksim.laba1.entity.transaction;

import java.io.Serial;
import java.io.Serializable;

public enum TransactionStatus implements Serializable {
    SUCCESS, FAILURE;
    @Serial
    private static final long serialVersionUID = 1234567L;
}
