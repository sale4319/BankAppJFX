package BankAppJFX;

/**
 *
 * @author Aco PC
 */
public class MasterCard extends Account {

    public MasterCard(int accountId, double initialDeposit) {
        super(accountId);
        this.setBalance(initialDeposit);
    }

    @Override
    public CardType getCardType() {
        return CardType.MasterCard;
    }

}
