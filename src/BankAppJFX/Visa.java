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
public class Visa extends Account {

    public Visa(int accountId, double initialDeposit) {
        super(accountId);
        this.setBalance(initialDeposit);
    }

    @Override
    public CardType getCardType() {
        return CardType.Visa;
    }

}
