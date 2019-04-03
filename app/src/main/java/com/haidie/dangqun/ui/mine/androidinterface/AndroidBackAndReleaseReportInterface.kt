package com.haidie.dangqun.ui.mine.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.mine.activity.EditMyReportActivity

/**
 * Create by   Administrator
 *      on     2018/08/31 15:11
 * description
 */
class AndroidBackAndReleaseReportInterface (activity: EditMyReportActivity){
    private val editMyReportActivity = activity
    @JavascriptInterface
    fun callAndroidCommonMethod(code: Int){
        editMyReportActivity.handleJSEvent(code)
    }
    @JavascriptInterface
    fun callAndroidReleaseSuccess(){
        editMyReportActivity.releaseSuccess()
    }
}