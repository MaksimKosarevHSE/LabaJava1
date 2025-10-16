package com.maksim.laba1.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Account implements Comparable<Account>, Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private int id;
    private Instant creationTime;
    private int ownerId;
    private Currency currency;
    private Long amount;
    private String name;
    private List<Integer> transactionIds;

    public Account(Instant creationTime, int ownerId, Currency currency, String name) {
        this.creationTime = creationTime;
        this.ownerId = ownerId;
        this.currency = currency;
        this.amount = 0L;
        this.name = name;
        transactionIds = new ArrayList<>();
    }

    public List<Integer> getTransactionIds() {
        return transactionIds;
    }

    public void setTransactionIds(List<Integer> transactionIds) {
        this.transactionIds = transactionIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void appendMoney(Long amount) {
        if (amount <= 0) throw new IllegalArgumentException("Сумма пополнения должна быть больше 0");
        this.amount += amount;
    }

    public void withdrawMoney(Long amount) {
        if (amount <= 0) throw new IllegalArgumentException("Сумма снятия должна быть больше 0");
        if (amount > this.amount) throw new IllegalArgumentException("На счету недостаточно средств");
        this.amount -= amount;
    }

    void addTransactionId(Integer id) {
        transactionIds.add(id);
    }

    @Override
    public int compareTo(Account o) {
        return this.id - o.id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", creationTime=" + creationTime +
                ", ownerId=" + ownerId +
                ", currency=" + currency +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                ", transactionIds=" + transactionIds +
                '}';
    }
}
