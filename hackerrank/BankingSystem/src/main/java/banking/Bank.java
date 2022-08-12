package banking;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Private Variables:<br>
 * {@link #accounts}: List&lt;Long, Account&gt;
 */
public class Bank implements BankInterface {
    private LinkedHashMap<Long, Account> accounts;
    AtomicLong accountNumberAtomic = new AtomicLong(1L);
    public Bank() {
        // complete the function
        accounts = new LinkedHashMap<>();

    }

    private Account getAccount(Long accountNumber) {
        // complete the function
        return accounts.getOrDefault(accountNumber, null);
    }

    public Long openCommercialAccount(Company company, int pin, double startingDeposit) {
        // complete the function
        synchronized (this){
            Long currentAccountNumber = accountNumberAtomic.getAndIncrement();
            CommercialAccount newCommercialAccount = new CommercialAccount(company, currentAccountNumber, pin, startingDeposit);
            accounts.put(currentAccountNumber, newCommercialAccount);
            return currentAccountNumber;
        }
    }

    public Long openConsumerAccount(Person person, int pin, double startingDeposit) {
        // complete the function
        synchronized (this){
            Long currentAccountNumber = accountNumberAtomic.getAndIncrement();
            ConsumerAccount newConsumerAccount = new ConsumerAccount(person, currentAccountNumber, pin, startingDeposit);
            accounts.put(currentAccountNumber, newConsumerAccount);
            return currentAccountNumber;
        }
    }
    public boolean authenticateUser(Long accountNumber, int pin) {
        // complete the function
        return accounts.containsKey(accountNumber) && accounts.get(accountNumber).validatePin(pin);
    }

    public double getBalance(Long accountNumber) {
        // complete the function
        if(getAccount(accountNumber) != null) return getAccount(accountNumber).getBalance();
        return 0.0;
    }

    public void credit(Long accountNumber, double amount) {
        // complete the function
        if(getAccount(accountNumber) != null) getAccount(accountNumber).creditAccount(amount);
      
    }

    public boolean debit(Long accountNumber, double amount) {
        // complete the function
        Account account = getAccount(accountNumber);
        synchronized (account){
            if (getAccount(accountNumber) != null) return account.debitAccount(amount);
        }
       return false;
    }
}
