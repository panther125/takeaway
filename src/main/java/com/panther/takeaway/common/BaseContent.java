package com.panther.takeaway.common;

/**
 * 基于ThreadLocal封装该线程的特殊数据
 */
public class BaseContent {

    private static ThreadLocal<Long> thread = new ThreadLocal<>();

    public static void setCurrentID(Long id){
        thread.set(id);
    }

    public static Long getCurrentID(){
        return thread.get();
    }
}
