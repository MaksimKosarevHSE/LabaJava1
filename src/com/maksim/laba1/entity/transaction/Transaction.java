package com.maksim.laba1.entity.transaction;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

abstract public class Transaction implements Comparable<Transaction>, Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private int id;
    private TransactionStatus status;
    private Instant executionTime;
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Instant executionTime) {
        this.executionTime = executionTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(Transaction o) {
        return this.id - o.id;
    }

}
