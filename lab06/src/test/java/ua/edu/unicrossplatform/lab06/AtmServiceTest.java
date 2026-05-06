package ua.edu.unicrossplatform.lab06;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtmServiceTest {
    @Test
    void depositAndWithdrawUpdateBalance() {
        AtmService atmService = new AtmService(new BigDecimal("200.00"), new OperationLog());
        atmService.openAccount("A-1", new BigDecimal("100.00"));

        atmService.deposit("A-1", new BigDecimal("25.00"));
        AccountSnapshot snapshot = atmService.withdraw("A-1", new BigDecimal("40.00"));

        assertEquals(new BigDecimal("85.00"), snapshot.balance());
    }

    @Test
    void rejectsWithdrawAboveLimit() {
        AtmService atmService = new AtmService(new BigDecimal("200.00"), new OperationLog());
        atmService.openAccount("A-1", new BigDecimal("500.00"));

        assertThrows(IllegalArgumentException.class,
                () -> atmService.withdraw("A-1", new BigDecimal("250.00")));
    }

    @Test
    void concurrentOperationsRemainConsistent() throws InterruptedException {
        AtmService atmService = new AtmService(new BigDecimal("200.00"), new OperationLog());
        atmService.openAccount("A-1", new BigDecimal("1000.00"));

        int operations = 10;
        CountDownLatch latch = new CountDownLatch(operations * 2);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < operations; i++) {
            threads.add(thread(latch, () -> atmService.deposit("A-1", new BigDecimal("10.00"))));
            threads.add(thread(latch, () -> atmService.withdraw("A-1", new BigDecimal("5.00"))));
        }

        for (Thread thread : threads) {
            thread.start();
        }
        latch.await();

        assertEquals(new BigDecimal("1050.00"), atmService.getAccount("A-1").balance());
    }

    private Thread thread(CountDownLatch latch, Runnable action) {
        return new Thread(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
    }
}
