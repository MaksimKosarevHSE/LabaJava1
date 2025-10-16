package com.maksim.laba1.entity.transaction;

public class Withdraw extends Transaction {
    private Integer accountId;
    private Long amount;

    public Withdraw(Integer accountId, Long amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Операция снятия {" +
                "Id счета=" + accountId +
                ", сумма снятия=" + amount +
                ", детали=" + getDescription() +
                ", статус=" + getStatus() +
                ", время выполнения=" + getExecutionTime() +
                '}';
    }
}
