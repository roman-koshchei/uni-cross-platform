package ua.edu.unicrossplatform.lab06;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public final class AtmDemo {
    private AtmDemo() {
    }

    public static void main(String[] args) throws InterruptedException {
        OperationLog operationLog = new OperationLog();
        AtmService atmService = new AtmService(new BigDecimal("200.00"), operationLog);
        atmService.openAccount("ACC-1001", new BigDecimal("500.00"));
        atmService.openAccount("ACC-1002", new BigDecimal("300.00"));

        CountDownLatch latch = new CountDownLatch(4);
        List<Thread> workers = List.of(
                worker("cashier-1", latch, () -> atmService.deposit("ACC-1001", new BigDecimal("50.00"))),
                worker("cashier-2", latch, () -> atmService.withdraw("ACC-1001", new BigDecimal("120.00"))),
                worker("cashier-3", latch, () -> atmService.deposit("ACC-1002", new BigDecimal("80.00"))),
                worker("cashier-4", latch, () -> atmService.withdraw("ACC-1002", new BigDecimal("70.00")))
        );

        for (Thread worker : workers) {
            worker.start();
        }
        latch.await();

        System.out.println("=== Лабораторна 6. Симуляція роботи банкомату ===");
        System.out.println("ACC-1001: " + atmService.getAccount("ACC-1001").balance());
        System.out.println("ACC-1002: " + atmService.getAccount("ACC-1002").balance());
        System.out.println("Кількість записів журналу: " + atmService.operationHistory().size());
    }

    private static Thread worker(String name, CountDownLatch latch, Runnable action) {
        return new Thread(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        }, name);
    }
}
