package com.maksim.laba1.entity.transaction;

public class Deposit extends Transaction {
    private Integer accountId;
    private Long amount;

    public Deposit(Integer accountId, Long amount) {
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
        return "Операция внесения {" +
                "Id счета=" + accountId +
                ", сумма внесения=" + amount +
                ", детали=" + getDescription() +
                ", статус=" + getStatus() +
                ", время выполнения=" + getExecutionTime() +
                '}';
    }
}
