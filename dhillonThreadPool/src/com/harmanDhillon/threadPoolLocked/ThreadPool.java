package com.harmanDhillon.threadPoolLocked;

import java.util.LinkedList;

/**
 * Nested Class:
 * <p>
 * WorkQueue stores a linked list of runnable tasks. Use the LinkedList
 * class for this. Note that this call is not thread safe, which
 * is why you must synchronize access to it(see what happens if you
 * do not). You need one method to ass a task to the list and another
 * to get a task. A thread will block if it tries to get a task when
 * none is available.
 * <p>
 * WorkerThread implements the Runnable interface. Its run method simply
 * tries to get a task from the queue and run it.
 * <p>
 * The ThreadPool constructor creates an array of threads and instantiates
 * the work queue. It then fills the array with WorkThread objects and starts
 * them. the size of the array should be determined by a static constant
 * (initialized to 3 for testing purposes). The only other method needed for
 * your ThreadPool class is one that adds a given task to the queue.
 * <p>
 * Created by Harman Dhillon on 4/8/2019.
 */
public class ThreadPool {
    private int threadPoolLimit;
    private WorkQueue workQueue;
    private Thread[] workerThreads;
    private int threadCount;

    public ThreadPool(int threadPoolLimit) {
        this.threadPoolLimit = threadPoolLimit;
        this.workQueue = new WorkQueue();
        this.workerThreads = new Thread[threadPoolLimit];
        this.threadCount = 0;

        for (int i = 0; i < this.threadPoolLimit; i++) {
            workerThreads[i] = new Thread(new WorkerThread());
            workerThreads[i].start();
        }
    }

    public synchronized void add(Connection newConnection) {
        workQueue.addTask(newConnection);
    }

    /**
     * WorkQueue stores a linked list of runnable tasks. Use the LinkedList
     * class for this. Note that this call is not thread safe, which
     * is why you must synchronize access to it(see what happens if you
     * do not). You need one method to ass a task to the list and another
     * to get a task. A thread will block if it tries to get a task when
     * none is available.
     */
    private class WorkQueue {

        LinkedList<Runnable> runnableList;

        public WorkQueue() {
            this.runnableList = new LinkedList<>();
        }

        private synchronized void addTask(Runnable task) {
            runnableList.add(task);
            notifyAll();
        }

        private synchronized Runnable getTask() {
            try {
                while (runnableList.isEmpty()) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return runnableList.remove();
        }
    }

    /**
     * WorkerThread implements the Runnable interface. Its run method simply
     * tries to get a task from the queue and run it.
     */
    private class WorkerThread implements Runnable {
        public void run() {
            while (true)
                workQueue.getTask().run();
        }
    }
}