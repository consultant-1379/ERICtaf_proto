package com.ericsson.cifwk.taf.dashboard.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by egergle on 05/01/2016.
 */
public class ThreadManager {

    public static Map<String, ThreadGroup> threadList = new HashMap();

    public static ThreadGroup get(String key) {
        return threadList.get(key);
    }

    public static void put(String key, ThreadGroup threads) {
        threadList.put(key, threads);
    }

    public static void removeItem(String key) {
        ThreadGroup threads =  threadList.get(key);
        stopThreads(threads);
        threadList.remove(key);
    }

    public static void stopThreads(ThreadGroup threads) {
        threads.interrupt();
    }

    public static void removeAll() {
        for (Map.Entry<String, ThreadGroup> entry : threadList.entrySet()) {
            ThreadGroup threads = threadList.get(entry.getKey());
            stopThreads(threads);
            threadList.remove(entry.getKey());
        }
    }


}
