/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BankAppJFX;

/**
 *
 * @author Aco PC
 */
public class TableModel {

    String id, cardNr, cardType, balance;

    public TableModel() {
    }

    public TableModel(String id, String cardNr, String cardType, String balance) {
        this.id = id;
        this.cardNr = cardNr;
        this.cardType = cardType;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardNr() {
        return cardNr;
    }

    public void setCardNr(String cardNr) {
        this.cardNr = cardNr;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

}
