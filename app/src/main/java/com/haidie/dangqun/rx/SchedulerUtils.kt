package com.haidie.dangqun.rx

/**
 * Created by admin2
 *  on 2018/08/09  14:25
 * description
 */
object SchedulerUtils {
    fun <T> ioToMain() : IoMainScheduler<T>{
        return IoMainScheduler()
    }
}