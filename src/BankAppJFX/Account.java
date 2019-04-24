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

    /**
     * @return the balance
     */
 
   

}
