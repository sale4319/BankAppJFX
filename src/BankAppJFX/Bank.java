package BankAppJFX;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


public class Bank implements Serializable {

    Account account;
    private DbService dbservice = new DbService();
    
    public int openAccount(String user, int cardNr, CardType cardType, double balance) {
        return dbservice.addCreditCard(user, cardNr, cardType, balance);
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }
  
}
