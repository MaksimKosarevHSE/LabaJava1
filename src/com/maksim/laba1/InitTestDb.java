package com.maksim.laba1;

import com.maksim.laba1.entity.*;
import com.maksim.laba1.entity.transaction.Deposit;
import com.maksim.laba1.entity.transaction.Transaction;
import com.maksim.laba1.entity.transaction.TransactionStatus;
import com.maksim.laba1.entity.transaction.Withdraw;

import java.io.*;

public class InitTestDb {
    public static void main(String[] args) throws IOException {
        //Инициализация "БД" и пример работы приложения без интерфейса



//        IdSequences idSequences = new IdSequences();
//        Bank bank = new Bank("Java");
//        bank.idSequences = idSequences;
//        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Entrypoint.DATABASE_FILE));
//        out.writeObject(bank);
//        out.close();
//
//        User user1 = bank.registrateUser("1", "1", "Maksim", "Kosarev");
//        User user2 = bank.registrateUser("3", "3", "Vasya", "Ivanov");
//
//        Account account1 = bank.createAccount(user1.getId(), Currency.RUBLE, "Bill1");
//        Account account2 = bank.createAccount(user2.getId(), Currency.DOLLAR, "Bill2");
//
//        Transaction trans = new Deposit(account1.getId(), 52L);
//        Transaction trans2 = new Deposit(account1.getId(), 2L);
//        Transaction trans3 = new Deposit(account1.getId(), 2000L);
//        Transaction trans4 = new Withdraw(account1.getId(), 100000000L);
//        Transaction trans5 = new Withdraw(account1.getId(), 2L);
//        Transaction trans6 = new Deposit(account1.getId(), 100L);
//        Transaction trans7 = new Deposit(account1.getId(), -100L);
//
//        bank.executeTransaction(trans);
//        bank.executeTransaction(trans2);
//        bank.executeTransaction(trans3);
//        bank.executeTransaction(trans4);
//        bank.executeTransaction(trans5);
//        bank.executeTransaction(trans6);
//        bank.executeTransaction(trans7);
//
//        Transaction trans777 = new Deposit(account2.getId(), 1000000L);
//        bank.executeTransaction(trans777);
//
//        var list = bank.getUserTransactions(1, (el) -> el.getStatus() == TransactionStatus.SUCCESS);
//        System.out.println(list);
    }
}
