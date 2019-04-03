package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.HousekeepingActivity

/**
 * Create by   Administrator
 *      on     2018/09/22 11:56
 * description
 */
class AndroidHousekeepingInterface(activity: HousekeepingActivity) {
    private val housekeepingActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        housekeepingActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun toHousekeepingList(url: String){
        housekeepingActivity.toHousekeepingList(url)
    }
    @JavascriptInterface
    fun toHousekeepingDetail(url: String){
        housekeepingActivity.toHousekeepingDetail(url)
    }
}