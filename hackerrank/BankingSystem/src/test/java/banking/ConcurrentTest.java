package banking;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ConcurrentTest {

    ExecutorService service;

    @Before
    public void setUp() {
        service = Executors.newFixedThreadPool(8);
    }

    @Test
    public void concurrentDebitForCharity() throws InterruptedException {
        final int amountOfCharities = 8000;
        final int amount = 10;

        final Bank bank = new Bank();
        final int pin = 12345;
        final Person person = new Person("Charitable", "Guy", pin);
        final long accountNumber = bank.openConsumerAccount(person, pin, amount * amountOfCharities);

        final CountDownLatch latch = new CountDownLatch(amountOfCharities);

        for (int i = 0; i < amountOfCharities; i++) {
            service.submit(() -> {
                if (bank.authenticateUser(accountNumber, pin)) {
                    bank.debit(accountNumber, amount);
                }

                latch.countDown();
            });
        }

        assertTrue("Timed out waiting for debits!", latch.await(5, TimeUnit.SECONDS));

        final double balance = bank.getBalance(accountNumber);
        assertEquals((person.getFirstName() + " " + person.getLastName())
                + " does not have an empty account after giving "
                + amountOfCharities + " donations of " + amount + "EUR!", 0d, balance, 0.0);
    }

    @Test
    public void concurrentConsumerAccounts() {
        final Bank bank = new Bank();
        final int maxAccounts = 10_000;

        final List<AccountIdent> accounts = IntStream.range(0, maxAccounts).parallel().mapToObj(identifier -> {
            final Person accountHolder = new Person("person-" + identifier, "test", identifier);
            return new AccountIdent(identifier, bank.openConsumerAccount(accountHolder, identifier, identifier));
        }).collect(Collectors.toList());

        for (long i = 1; i <= maxAccounts; i++) {
            final long lookFor = i;
            assertTrue("Account id " + i + " was not found!",
                    accounts.stream().anyMatch(ident -> ident.accountId == lookFor));
        }

        for (AccountIdent ident : accounts) {
            // make a transaction on every account
            Transaction transaction = null;
            try {
                transaction = new Transaction(bank, ident.accountId, ident.identifier);
            } catch (Exception e) {
                fail("Could not authenticate account " + ident.accountId + " with pin: " + ident.identifier);
            }

            // remove balance
            assertTrue(transaction.debit(ident.identifier));

            final double balance = bank.getBalance(ident.accountId);
            assertEquals(0d, balance, 0.0);
        }
    }

    @After
    public void teardown() throws InterruptedException {
        service.shutdown();
        if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
            service.shutdownNow();
        }
    }

    private static class AccountIdent {
        Integer identifier;
        Long accountId;

        AccountIdent(Integer identifier, Long accountId) {
            this.identifier = identifier;
            this.accountId = accountId;
        }
    }
}
