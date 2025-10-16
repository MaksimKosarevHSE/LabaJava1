package com.maksim.laba1;

import com.maksim.laba1.entity.Account;
import com.maksim.laba1.entity.Bank;
import com.maksim.laba1.entity.Currency;
import com.maksim.laba1.entity.User;
import com.maksim.laba1.entity.transaction.Deposit;
import com.maksim.laba1.entity.transaction.Transaction;
import com.maksim.laba1.entity.transaction.TransactionStatus;
import com.maksim.laba1.entity.transaction.Withdraw;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.util.Scanner;
import java.util.function.Predicate;

public class Entrypoint {
    public static final String DATABASE_FILE = "database.bin";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Десериализуем данные из файла
        Bank bank;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
            bank = (Bank) ois.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            printError("Не удалось загрузить данные");
            return;
        }

        Integer action = null;
        int menuId = 0;
        User currentUser = null;
        Account currentAccount = null;

        // Ниже представлена консольная менюшка, очень много ветвлений, но, к сожалению, консольную проще не сделать
        while (true) {
            showFrame(menuId, currentUser, bank, currentAccount);
            // Т.к. пользователь часто вводит числа, то решил глобальным try отлавливать некорректный ввод чисел
            try {
                action = Integer.parseInt(sc.nextLine());
                if (menuId == 0) { //для гостей
                    if (action == 2) {
                        System.out.println("Введите имя:");
                        String firstName = sc.nextLine();
                        System.out.println("Введите фамилию:");
                        String lastName = sc.nextLine();
                        System.out.println("Введите почту:");
                        String email = sc.nextLine();
                        System.out.println("Введите пароль:");
                        String password = sc.nextLine();
                        try {
                            currentUser = bank.registrateUser(email, password, firstName, lastName);
                            menuId = 1;
                        } catch (IllegalArgumentException e) {
                            printError(e.getMessage());
                        }
                    } else if (action == 1) {
                        System.out.println("Введите почту:");
                        String email = sc.nextLine();
                        System.out.println("Введите пароль:");
                        String password = sc.nextLine();
                        try {
                            currentUser = bank.loginUser(email, password);
                            menuId = 1;
                        } catch (IllegalArgumentException e) {
                            printError(e.getMessage());
                        }
                    } else if (action == 3) {
                        return;
                    }
                } else if (menuId == 1) {
                    if (action == 4) {
                        currentUser = null;
                        menuId = 0;
                    } else if (action == 2) {
                        System.out.println(StaticMessages.addAccount);
                        int inp1 = Integer.parseInt(sc.nextLine()) - 1;
                        Currency currency;
                        if (0 <= inp1 && inp1 <= 2) {
                            currency = Currency.values()[inp1];
                            System.out.println(StaticMessages.addAccount2);
                            String accName = sc.nextLine();
                            try {
                                bank.createAccount(currentUser.getId(), currency, accName);
                                printSuccess("Счёт успешно создан!");
                            } catch(IllegalArgumentException ex){
                                printError(ex.getMessage());
                            }
                        } else {
                            printError("Введен некорректный номер валюты");
                        }
                    } else if (action == 1) {
                        menuId = 3;
                    } else if (action == 3) {
                        menuId = 100;
                    }
                } else if (menuId == 3) {
                    if (action == currentUser.getAccountsId().size() + 1) {
                        menuId = 1;
                    } else if (1 <= action && action <= currentUser.getAccountsId().size()) {
                        currentAccount = bank.getUserAccounts(currentUser.getId()).get(action - 1);
                        menuId = 4;
                    }
                } else if (menuId == 4) {
                    if (action == 4) {
                        menuId = 3;
                    } else if (action == 1) {
                        printSuccess("Баланс: " + currentAccount.getAmount() + currentAccount.getCurrency().getSymbol());
                    } else if (action == 2) {
                        System.out.println("Введите сумму для снятия:");
                        Long sm = Long.parseLong(sc.nextLine());
                        Transaction trans = new Withdraw(currentAccount.getId(), sm);
                        trans = bank.executeTransaction(trans);
                        if (trans.getStatus() == TransactionStatus.FAILURE) {
                            printError(trans.getDescription());
                        } else {
                            printSuccess("Средства успешно сняты");
                        }
                        //снимаем деньги
                    } else if (action == 3) {
                        System.out.println("Введите сумму для внесения на счёт:");
                        Long sm = Long.parseLong(sc.nextLine());
                        Transaction trans = new Deposit(currentAccount.getId(), sm);
                        trans = bank.executeTransaction(trans);
                        if (trans.getStatus() == TransactionStatus.FAILURE) {
                            printError(trans.getDescription());
                        } else {
                            printSuccess("Средства успешно внесены");
                        }
                        // вносим деньги
                    }
                } else if (menuId == 100) {
                    String msg = null;
                    Predicate<Transaction> predicate = null;
                    boolean isThereIOException = false;
                    switch (action) {
                        case 1:
                            msg = "Ваша история пополнения:";
                            predicate = el -> el instanceof Deposit;
                            break;
                        case 2:
                            msg = "Ваша история снятия:";
                            predicate = el -> el instanceof Withdraw;
                            break;
                        case 3:
                            System.out.println("Введите диапазон суммы двумя числами (например 200 350)");
                            long lowerBound;
                            long upperBound;
                            try {
                                String[] inp = sc.nextLine().split(" ");
                                lowerBound = Long.parseLong(inp[0]);
                                upperBound = Long.parseLong(inp[1]);
                                if (lowerBound > upperBound) throw new Exception();
                            } catch (Exception ex) {
                                printError("Диапазон сумм введен некорректно");
                                isThereIOException = true;
                                break;
                            }
                            msg = "Ваша история пополнения:";
                            predicate = el -> el.getStatus() == TransactionStatus.SUCCESS && (el instanceof Deposit depo) && lowerBound <= depo.getAmount() && depo.getAmount() <= upperBound;
                            break;
                        case 4:
                            System.out.println("Введите диапазон суммы двумя числами (например 200 350)");
                            try {
                                String[] inp = sc.nextLine().split(" ");
                                lowerBound = Long.parseLong(inp[0]);
                                upperBound = Long.parseLong(inp[1]);
                                if (lowerBound > upperBound) throw new Exception();
                            } catch (Exception ex) {
                                printError("Диапазон введен некорректно");
                                isThereIOException = true;
                                break;
                            }
                            msg = "Ваша история снятия:";
                            predicate = el -> el.getStatus() == TransactionStatus.SUCCESS && (el instanceof Withdraw depo) && lowerBound <= depo.getAmount() && depo.getAmount() <= upperBound;
                            break;
                        case 5:
                            msg = "Ваши успешные транзакции:";
                            predicate = el -> el.getStatus() == TransactionStatus.SUCCESS;
                            break;
                        case 6:
                            msg = "Ваши неуспешные транзакции:";
                            predicate = el -> el.getStatus() == TransactionStatus.FAILURE;
                            break;
                        case 7:
                            System.out.println("Введите диапазон даты и времени одной строкой в формате: 2025-10-05T13:00:00Z 2025-10-11T17:30:00Z ");
                            Instant start;
                            Instant end;
                            try {
                                String[] times = sc.nextLine().split(" ");
                                start = Instant.parse(times[0]);
                                end = Instant.parse(times[1]);
                            } catch (Exception ex) {
                                isThereIOException = true;
                                printError("Период введен в неверном формате");
                                break;
                            }
                            msg = "Ваши транзакции:";
                            predicate = el -> el.getStatus() == TransactionStatus.SUCCESS && start.compareTo(el.getExecutionTime()) <= 0 && el.getExecutionTime().compareTo(end) <= 0;
                            break;
                        case 8:
                            menuId = 1;
                            break;
                    }
                    if (!isThereIOException && predicate != null) {
                        System.out.println(msg);
                        var result = bank.getUserTransactions(currentUser.getId(), predicate);
                        for (var el : result) {
                            if (el instanceof Deposit dep){
                                System.out.println("Операция внесения {" +
                                        "имя счёта=" + bank.getAccountName(dep.getAccountId())+
                                        ", сумма внесения=" + dep.getAmount() +
                                        ", детали=" + dep.getDescription() +
                                        ", статус=" + dep.getStatus() +
                                        ", время выполнения=" + dep.getExecutionTime() +
                                        '}');
                            } else if (el instanceof Withdraw wit){
                                System.out.println("Операция снятия {" +
                                        "имя счёта=" + bank.getAccountName(wit.getAccountId()) +
                                        ", сумма снятия=" + wit.getAmount() +
                                        ", детали=" + wit.getDescription() +
                                        ", статус=" + wit.getStatus() +
                                        ", время выполнения=" + wit.getExecutionTime() +
                                        '}');
                            }

                        }
                        if (result.isEmpty()) System.out.println("Пусто");
                    }
                }
            } catch (NumberFormatException ex) {
                printError("Введено не число");
            }
        }

    }


    private static void printError(String msg) {
        System.out.println(StaticMessages.ANSI_RED + msg + StaticMessages.ANSI_RESET);
    }

    private static void printSuccess(String msg) {
        System.out.println(StaticMessages.ANSI_GREEN + msg + StaticMessages.ANSI_RESET);

    }

    // Отрисовка диалогового окна на каждой итерации while
    private static void showFrame(Integer menuId, User currentUser, Bank bank, Account account) {
        System.out.println();
        if (menuId == 0) {
            System.out.printf(StaticMessages.forGuest, bank.getName());
        } else if (menuId == 1) {
            System.out.printf(StaticMessages.mainMenu, currentUser.getFirstName());
        } else if (menuId == 3) {
            StringBuilder sb = new StringBuilder();
            sb.append(StaticMessages.accountsMenuStart);
            int i = 1;
            for (var el : bank.getUserAccounts(currentUser.getId())) {
                sb.append(i++).append(". ").append(el.getName()).append(" (").append(el.getCurrency().getSymbol()).append(")\n");
            }
            sb.append(i).append(". Назад\n");
            sb.append(StaticMessages.accountMenuEnd);
            System.out.print(sb);
        } else if (menuId == 4) {
            System.out.printf(StaticMessages.accountMenu, account.getName(), account.getCurrency().getSymbol());
        } else if (menuId == 100) {
            System.out.print(StaticMessages.attrsSearch);
        }
    }
}

