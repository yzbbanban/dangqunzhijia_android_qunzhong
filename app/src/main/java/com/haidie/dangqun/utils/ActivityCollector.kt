package com.haidie.dangqun.utils

import android.app.Activity



/**
 * Created by admin2
 *  on 2018/08/22  10:03
 * description   一键退出App
 */
class ActivityCollector {

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityCollector()
        }
    }

    private  var allActivities = mutableSetOf<Activity>()

    fun addActivity(act: Activity) {
        allActivities.add(act)
    }

    fun removeActivity(act: Activity) {
        allActivities.remove(act)
    }

     @Synchronized fun exitApp() {
        allActivities.run {
            for (act in allActivities) {
                act.finish()
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }
}