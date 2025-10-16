package com.maksim.laba1.entity;

import com.maksim.laba1.Entrypoint;
import com.maksim.laba1.entity.transaction.Deposit;
import com.maksim.laba1.entity.transaction.Transaction;
import com.maksim.laba1.entity.transaction.TransactionStatus;
import com.maksim.laba1.entity.transaction.Withdraw;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;

// Все операции (регистрация, авторизация, транзакции) происходят через этот класс
public class Bank implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private String name;
    public Map<Integer, User> users;
    public Map<Integer, Account> accounts;
    public Map<Integer, Transaction> transactions;
    public IdSequences idSequences;

    private void update() { // Обновление объекта в файле
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Entrypoint.DATABASE_FILE))) {
            oos.writeObject(this);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Не удалось обновить базу данных, возможна потеря данных, принудительное завершение");
        }
    }

    public Bank(String name) {
        this.name = name;
        this.users = new TreeMap<>();
        this.accounts = new TreeMap<>();
        this.transactions = new TreeMap<>();
        this.idSequences = new IdSequences();
    }

    public Account createAccount(Integer userId, Currency currency, String name) {
        if (accounts.values().stream().anyMatch(el -> el.getName().equals(name) && el.getOwnerId() == userId)) throw new IllegalArgumentException("Счёт с таким именем уже существует");
        if (name.isBlank()) throw new IllegalArgumentException("Введите название счёта");
        Account account = new Account(Instant.now(), userId, currency, name);
        account.setId(idSequences.incrementAccountId());
        accounts.put(account.getId(), account);
        users.get(userId).addAccount(account.getId());
        update();
        return account;
    }

    public User registrateUser(String email, String password, String firstName, String lastName) {
        validateCredentials(email, password, firstName, lastName);
        User user = findUser(email);
        if (user != null) throw new IllegalArgumentException("Пользователя с таким email уже существует");
        user = new User(email, password, firstName, lastName);
        user.setId(idSequences.incrementUserId());
        users.put(user.getId(), user);
        update();
        return user;
    }

    public User loginUser(String email, String password) {
        User user = findUser(email);
        if (user == null) throw new IllegalArgumentException("Пользователя с таким email не существует");
        if (!user.getPassword().equals(password)) throw new IllegalArgumentException("Неверный пароль");
        return user;
    }

    public Transaction executeTransaction(Transaction transaction) {
        transaction.setId(idSequences.incrementTransactionId());
        try {
            if (transaction instanceof Deposit deposit) {
                executeDepositTransaction(deposit);
            } else if (transaction instanceof Withdraw withdraw) {
                executeWithdrawTransaction(withdraw);
            }
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setDescription("нет");
        } catch (IllegalArgumentException e) {
            transaction.setStatus(TransactionStatus.FAILURE);
            transaction.setDescription(e.getMessage());
        }
        transaction.setExecutionTime(Instant.now());
        transactions.put(transaction.getId(), transaction);

        update();
        return transaction;
    }

    private void executeDepositTransaction(Deposit deposit) {
        Account account = accounts.get(deposit.getAccountId());
        account.addTransactionId(deposit.getId());
        account.appendMoney(deposit.getAmount());
    }

    private void executeWithdrawTransaction(Withdraw withdraw) {
        Account account = accounts.get(withdraw.getAccountId());
        account.addTransactionId(withdraw.getId());
        account.withdrawMoney(withdraw.getAmount());
    }


    private void validateCredentials(String email, String password, String firstName, String lastName) {
        if (email == null || password == null
                || firstName == null || lastName == null)
            throw new IllegalArgumentException("Заолпните необохдимые поля");
        if (email.isBlank() || password.isBlank()
                || firstName.isBlank() || lastName.isBlank())
            throw new IllegalArgumentException("Заполните необоходимые поля");
    }

    private User findUser(String email) {
        return (User) users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    public List<Account> getUserAccounts(Integer userId) {
        User user = users.get(userId);
        List<Account> ans = new ArrayList<>();
        for (var el : user.getAccountsId()) {
            ans.add(accounts.get(el));
        }
        return ans;
    }

    // Сделал фильтрацию через функциональный интерфейс
    public List<Transaction> getUserTransactions(Integer userId, Predicate<Transaction> predicate) {
        return getUserAccounts(userId).stream().flatMap(el -> el.getTransactionIds().stream()).map(el -> transactions.get(el)).filter(predicate).toList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountName(Integer accountId) {
        return accounts.get(accountId).getName();
    }
}
