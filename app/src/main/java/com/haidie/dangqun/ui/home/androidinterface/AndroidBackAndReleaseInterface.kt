package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.ProblemReportActivity

/**
 * Create by   Administrator
 *      on     2018/08/31 15:11
 * description
 */
class AndroidBackAndReleaseInterface (activity: ProblemReportActivity){
    private val problemReportActivity = activity
    @JavascriptInterface
    fun callAndroidCommonMethod(code: Int){
        problemReportActivity.handleJSEvent(code)
    }
    @JavascriptInterface
    fun callAndroidReleaseSuccess(){
        problemReportActivity.releaseSuccess()
    }
}