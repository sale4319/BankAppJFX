/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BankAppJFX;

import java.io.Serializable;

/**
 *
 * @author Aco PC
 */
public abstract class Account implements Serializable {

    private double balance = 0;
    private int accountNumber;

    Account(int accountId) {
        accountNumber = accountId;
    }

    public abstract CardType getCardType();

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }
}
