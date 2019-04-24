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
public class InvalidAmountException extends Exception {

    public InvalidAmountException() {
        super("Invalid mount for transaction.");
    }

}
