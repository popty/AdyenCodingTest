package banking;

import java.util.HashSet;

public abstract class AccountHolder {
    private int idNumber;
    private HashSet<Integer> idNumbers = new HashSet<>();

    /**
     * @param idNumber The government-issued ID used during account setup.
     */
    public AccountHolder(int idNumber) {
        // complete the function
        if(idNumbers.add(idNumber))
          this.idNumber = idNumber;
    }

    /**
     * @return private int {@link AccountHolder#idNumber}
     */
    public int getIdNumber() {
        // complete the function

        return this.idNumber;
    }
}
