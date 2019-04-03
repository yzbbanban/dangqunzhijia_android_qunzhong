package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.FaultRepairActivity

/**
 * Create by   Administrator
 *      on     2018/09/22 11:56
 * description
 */
class AndroidFaultRepairInterface(activity: FaultRepairActivity) {
    private val faultRepairActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        faultRepairActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun callAndroid(url: String){
        faultRepairActivity.goToDetail(url)
    }
    @JavascriptInterface
    fun releaseWorkOrder(url: String){
        faultRepairActivity.releaseWorkOrder(url)
    }
    @JavascriptInterface
    fun toEditReport(url: String){
        faultRepairActivity.toEditReport(url)
    }
}