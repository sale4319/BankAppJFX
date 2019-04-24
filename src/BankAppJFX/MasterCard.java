package BankAppJFX;

/**
 *
 * @author Aco PC
 */
public class MasterCard extends Account {

    public MasterCard(int AccountId, double initialDeposit) {
        super(AccountId);
        
    }

    @Override
    public CardType getCardType() {
        return CardType.MasterCard;
    }

}
