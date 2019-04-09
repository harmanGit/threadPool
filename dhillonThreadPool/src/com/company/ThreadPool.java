package com.company;

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
    private int numberOfConnections;
    private WorkQueue workQueue;
    private Thread[] workerThreads;

    public ThreadPool(int threadPoolLimit) {
        this.threadPoolLimit = threadPoolLimit;
        this.workQueue = new WorkQueue();
        this.numberOfConnections = 0;
        workerThreads = new Thread[threadPoolLimit];

        for (int i = 0; i < this.threadPoolLimit; i++)
            workerThreads[i] = new Thread(new WorkerThread());


    }

    public void add(Connection newConnection) {
        workQueue.addTask(newConnection);

//        if(workerThreads[numberOfConnections] != null && !workerThreads[numberOfConnections].isAlive()){
//            workQueue.runnableList.remove(numberOfConnections);
            workerThreads[numberOfConnections].start();
//        }
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
//            if(numberOfConnections <= threadPoolLimit){
                runnableList.add(task);
                numberOfConnections = runnableList.size() - 1;
                System.err.println("asdfasd: "+ numberOfConnections);
//            }
//            return false;
        }

        private Runnable getTask(int indexOfTask) {
            return runnableList.get(indexOfTask);
        }
    }

    /**
     * WorkerThread implements the Runnable interface. Its run method simply
     * tries to get a task from the queue and run it.
     */
    private class WorkerThread implements Runnable {
        public void run() {
            workQueue.getTask(numberOfConnections).run();
        }
    }
}
