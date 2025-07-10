package ru.dse.java_pro.task3;

import java.util.*;
import java.util.concurrent.locks.*;

public class CustomThreadPool {

    private final LinkedList<Runnable> taskQueue = new LinkedList<>();
    private final List<Worker> workers = new ArrayList<>();
    private final int capacity;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition taskAvailable = lock.newCondition();

    private volatile boolean isShutdown = false;

    public CustomThreadPool(int capacity) {
        this.capacity = capacity;
        for (int i = 0; i < capacity; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            worker.start();
        }
    }

    public void execute(Runnable task) {
        lock.lock();
        try {
            if (isShutdown) {
                throw new IllegalStateException("Пул потоков остановлен.  Новые задачи не принимаются.");
            }
            taskQueue.addLast(task);
            taskAvailable.signal(); // уведомить один ожидающий поток
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();
        try {
            isShutdown = true;
            taskAvailable.signalAll(); // разбудить все потоки, чтобы они могли завершить работу
        } finally {
            lock.unlock();
        }
    }

    public void awaitTermination() {
        for (Worker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private class Worker extends Thread {
        public void run() {
            while (true) {
                Runnable task;
                lock.lock();
                try {
                    while (taskQueue.isEmpty()) {
                        if (isShutdown) return; // Нет больше задач для обработки - выход из потока
                        try {
                            taskAvailable.await();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    task = taskQueue.removeFirst();
                } finally {
                    lock.unlock();
                }

                try {
                    task.run();
                } catch (Throwable t) {
                    System.err.println("Выполнить задачу не удалось: " + t.getMessage());
                }
            }
        }
    }
}
