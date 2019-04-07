package BankAppJFX;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Bank implements Serializable {

    private DbService database = new DbService();

    /*int openAccount(String firstName, String lastName, String ssn, CardType cardType, double balance) {
        return database.AddAccount(firstName, lastName, ssn, cardType, balance);
    }

    Customer getCustomer(int accountId) {
        return database.GetAccount(accountId);
    }

    ArrayList<Customer> getCustomers() {
        return database.GetAllAccounts();
    }

    boolean closeAccount(int accountId) {
        return database.DeleteAccount(accountId);
    }*/

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }

   /* public void withdraw(int accountId, double amount) throws InsufficientFundsException {
        Customer customer = getCustomer(accountId);
        double transactionFee = getTransactionFee(customer.getAccount().getAccountType());
        if (amount + transactionFee > customer.getAccount().getBalance()) {
            throw new InsufficientFundsException();
        }
        double newBalance = customer.getAccount().getBalance() - (amount + transactionFee);
        database.UpdateAccount(accountId, newBalance);
    }

    public void deposit(int accountId, double amount) throws InvalidAmountException {
        Customer customer = getCustomer(accountId);
        if (amount <= 0) {
            throw new InvalidAmountException();
        }

        double interest = checkInterest(customer.getAccount().getBalance(), amount);
        double amountToDeposit = amount + (amount * interest);
        database.UpdateAccount(accountId, customer.getAccount().getBalance() + amountToDeposit);

    }*/

    public double checkInterest(double balance, double amount) {
        double interest = 0;
        if (balance + amount > 10000) {
            interest = 0.05;

        } else {
            interest = 0.02;
        }
        return interest;
    }

    public double getTransactionFee(CardType cardType) {
        double transactionFee = 0;
        switch (cardType) {
            case Visa:
            case MasterCard:
                transactionFee = 5;
                break;
            case Undefined:
            default:
                transactionFee = 0;
                break;
        }
        return transactionFee;
    }
}
