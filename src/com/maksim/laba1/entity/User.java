package com.maksim.laba1.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User implements Comparable<User>, Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Integer> accountsId;

    public User(String email, String password, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.accountsId = new LinkedList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public List<Integer> getAccountsId() {
        return accountsId;
    }

    public void setAccountsId(List<Integer> accountsId) {
        this.accountsId = accountsId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String hashPassword) {
        this.password = hashPassword;
    }

    @Override
    public int compareTo(User o) {
        return this.id - o.id;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountsId=" + accountsId +
                '}';
    }

    public void addAccount(Integer accountId) {
        accountsId.add(accountId);
    }
}
