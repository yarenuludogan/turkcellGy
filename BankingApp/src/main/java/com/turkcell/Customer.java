package com.turkcell;

public class Customer {
    private int id;
    private String name;
    private double balance;
    private String password;


public Customer(int id, String name,double balance, String password) {
    this.id = id;
    this.name = name;
    this.balance = balance;
    this.password = password;
}
public int getId() {
    return id;
}
public void setId(int id) {
    this.id = id;
}
public String getName() {
    return name;
}
public double getBalance() {
    return balance;
}
public String getPassword() {
    return password;
}

@Override
public String toString() {
        return ("Customer id=" + id + ", name='" + name + "', balance=" + balance );
}

public void deposit(double amount){
    if (amount>0){
        balance += amount;
    }
}
public void withdraw(double amount){
    if (amount>0 && balance>=amount){
        balance -= amount;
    }
}
}