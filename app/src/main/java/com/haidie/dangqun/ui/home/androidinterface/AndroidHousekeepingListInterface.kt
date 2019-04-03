package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.HousekeepingListActivity

/**
 * Create by   Administrator
 *      on     2018/09/22 11:56
 * description
 */
class AndroidHousekeepingListInterface(activity: HousekeepingListActivity) {
    private val housekeepingListActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        housekeepingListActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun toHousekeepingDetail(url: String){
        housekeepingListActivity.toHousekeepingDetail(url)
    }
}