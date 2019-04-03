package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.ReleaseWorkOrderActivity

/**
 * Create by   Administrator
 *      on     2018/08/31 15:11
 * description
 */
class AndroidReleaseWorkOrderInterface (activity: ReleaseWorkOrderActivity){
    private val releaseWorkOrderActivity = activity
    @JavascriptInterface
    fun callAndroidCommonMethod(code: Int){
        releaseWorkOrderActivity.handleJSEvent(code)
    }
    @JavascriptInterface
    fun callAndroidReleaseSuccess(){
        releaseWorkOrderActivity.releaseSuccess()
    }
}