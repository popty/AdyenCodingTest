package banking;

/**
 * The public methods for the {@link Account} class
 */
public interface AccountInterface {

    /**
     * @return The {@link AccountHolder}.
     */
    AccountHolder getAccountHolder();

    /**
     * @param attemptedPin The attempted PIN.
     * @return true if attemptedPin matches the account; otherwise, return false.
     */
    boolean validatePin(int attemptedPin);

    /**
     * @return {@link Account#accountBalance}.
     */
    double getBalance();

    /**
     * @return {@link Account#accountNumber}
     */
    Long getAccountNumber();

    /**
     * @param amount The amount to be deposited into the account.
     */
    void creditAccount(double amount);

    /**
     * @param amount The amount to be withdrawn from the account.
     * @return true if amount could be withdrawn; otherwise, return false.
     */
    boolean debitAccount(double amount);
}
