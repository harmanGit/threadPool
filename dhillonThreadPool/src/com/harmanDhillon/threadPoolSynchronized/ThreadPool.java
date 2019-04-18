package com.harmanDhillon.threadPoolSynchronized;

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
    private int threadPoolLimit;//max number of connections that can connect to the server (max number of threads)
    private WorkQueue workQueue;//stores a linked list of runnable tasks (connections)

    /**
     * The ThreadPool constructor creates an array of threads and instantiates
     * the work queue. It then fills the array with WorkThread objects and starts
     * them. the size of the array should be determined by a static constant
     * (initialized to 3 for testing purposes).
     * @param threadPoolLimit <code>int</code> max number of connections that can connect
     *                       to the server (max number of threads)
     */
    public ThreadPool(int threadPoolLimit) {
        this.threadPoolLimit = threadPoolLimit;
        this.workQueue = new WorkQueue();
        Thread[] workerThreads = new Thread[threadPoolLimit];
        //filling the array with WorkThread objects and then starting them
        for (int i = 0; i < this.threadPoolLimit; i++) {
            workerThreads[i] = new Thread(new WorkerThread());
            workerThreads[i].start();
        }
    }

    /**
     * Method adds the given connection to the workQueue.
     * @param newConnection <code>Connection</code> new connection to be added
     */
    public void add(Connection newConnection) {
        workQueue.addTask(newConnection);
    }

    /**
     * WorkQueue stores a linked list of runnable tasks. Use the LinkedList
     * class for this. Note that this call is not thread safe, which
     * is why you must synchronize access to it (done via synchronized).
     * You need one method to ass a task to the list and another
     * to get a task. A thread will block if it tries to get a task when
     * none is available.
     */
    private class WorkQueue {

        LinkedList<Runnable> runnableList;//stores a linked list of runnable tasks (connections)

        /**
         * Constructor initializes the runnableList object with a empty LinkedList
         */
        public WorkQueue() {
            this.runnableList = new LinkedList<>();
        }

        /**
         * Method is used to add a new task (connection) to the LinkedList. Synchronization insures that two threads
         * can not execute this method at the same time.
         * @param task <code>Connection</code> the runnable task (connection) to add to the LinkedList
         */
        private synchronized void addTask(Runnable task) {
            runnableList.add(task);
            notifyAll();//wakes all threads that were currently waiting on this
        }

        /**
         * Method gets all task from the linkedList and in the process removes them. Synchronization insures that two
         * threads can not execute this method at the same time. Also method will wait if there are no more tasks in
         * the Queue (linkedList)
         * @return <code>Runnable</code> task (connection)
         */
        private synchronized Runnable getTask() {
            try {
                while (runnableList.isEmpty())
                    wait();//waiting because there are no more tasks in the Queue (linkedList)
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
            while (true)//calls and executes the connections objects run() method, forever
                workQueue.getTask().run();
        }
    }
}