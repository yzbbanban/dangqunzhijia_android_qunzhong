package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.EditReportActivity

/**
 * Create by   Administrator
 *      on     2018/08/31 15:11
 * description
 */
class AndroidBackAndReleaseReportInterface (activity: EditReportActivity){
    private val editReportActivity = activity
    @JavascriptInterface
    fun callAndroidCommonMethod(code: Int){
        editReportActivity.handleJSEvent(code)
    }
    @JavascriptInterface
    fun callAndroidReleaseSuccess(){
        editReportActivity.releaseSuccess()
    }
}