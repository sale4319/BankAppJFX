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
public class Customer implements Serializable {

    private final String firstName;
    private final String lastName;
    private final Account account;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    Customer(String firstName, String lastName, Account account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.account = account;
    }

    Account getAccount() {
        return account;
    }
}
