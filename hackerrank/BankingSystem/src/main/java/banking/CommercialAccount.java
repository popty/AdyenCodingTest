package banking;

import java.util.HashSet;
import java.util.Set;

/**
 * Account implementation for commercial (business) customers.<br><br>
 * <p>
 * Private Variables:<br>
 * {@link #authorizedUsers}: List&lt;Person&gt;<br>
 */
public class CommercialAccount extends Account {
    private Set<Person> authorizedUsers;

    public CommercialAccount(Company company, Long accountNumber, int pin, double startingDeposit) {
        // complete the function
        super(company, accountNumber, pin, startingDeposit);
        this.authorizedUsers = new HashSet<Person>();
        
    }

    /**
     * @param person The authorized user to add to the account.
     */
    protected void addAuthorizedUser(Person person) {
        // complete the function
        authorizedUsers.add(person);
    }

    /**
     * @param person
     * @return true if person matches an authorized user in {@link #authorizedUsers}; otherwise, false.
     */
    public boolean isAuthorizedUser(Person person) {
        // complete the function
        return authorizedUsers.contains(person);
    }
}
