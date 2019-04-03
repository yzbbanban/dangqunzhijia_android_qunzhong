package com.haidie.dangqun.ui.mine.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.mine.activity.MyReportActivity

/**
 * Create by   Administrator
 *      on     2018/09/11 15:09
 * description
 */
class AndroidMyReportInterface(activity: MyReportActivity) {
    private val myReportActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        myReportActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun callAndroid(url: String){
        myReportActivity.goToDetail(url)
    }
    @JavascriptInterface
    fun toEditMyReport(url: String){
        myReportActivity.toEditMyReport(url)
    }
}