package ru.dse.java_pro.task3;

public class Main {
    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(2);

        for (int i = 0; i < 10; i++) {
            int taskId = i;
            pool.execute(() -> {
                System.out.println("Выполняется задача " + taskId + " в потоке " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination();
        System.out.println("Все задачи выполнены.");
    }
}