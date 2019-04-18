package com.harmanDhillon.threadPoolLocked;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
    private ReentrantLock reentrantLock;//used to lock, and unlock a thread (traditional way to synchronize threads)
    private Condition condition;//used to monitor the threads

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
        this.reentrantLock = new ReentrantLock();
        this.condition = reentrantLock.newCondition();

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
     * is why you must synchronize access to it (done via the ReentrantLock and Condition).
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
         * Method is used to add a new task (connection) to the LinkedList. ReentrantLock and Condition
         * are used to insures that two threads can not execute this method at the same time.
         * @param task <code>Connection</code> the runnable task (connection) to add to the LinkedList
         */
        private void addTask(Runnable task) {
            reentrantLock.lock();//locking up the thread (Acquires the lock)
            runnableList.add(task);//adding to the WorkerQueue
            condition.signalAll();//Wakes up all waiting threads
            reentrantLock.unlock();//unlocking the thread(Attempts to release this lock)
        }

        /**
         * Method gets all task from the linkedList and in the process removes them. S ReentrantLock and Condition
         * are used to insures that two threads can not execute this method at the same time. Also method will
         * wait if there are no more tasks in the Queue (linkedList)
         * @return <code>Runnable</code> task (connection)
         */
        private Runnable getTask() {
            try {
                reentrantLock.lock();//locking up the thread (Acquires the lock.)
                while (runnableList.isEmpty())
                    condition.await();//waiting because there are no more tasks in the Queue (linkedList)
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();//unlocking the thread(Attempts to release this lock)
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