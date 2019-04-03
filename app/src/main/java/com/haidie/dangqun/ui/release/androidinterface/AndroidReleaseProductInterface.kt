package com.haidie.dangqun.ui.release.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.release.activity.ReleaseProductActivity

/**
 * Create by   Administrator
 *      on     2018/09/03 10:25
 * description
 */
class AndroidReleaseProductInterface(activity: ReleaseProductActivity) {
    private val releaseProductActivity: ReleaseProductActivity = activity
    @JavascriptInterface
    fun callAndroidCommonMethod(code: Int){
        releaseProductActivity.handleJSEvent(code)
    }
    @JavascriptInterface
    fun callAndroidReleaseSuccess(){
        releaseProductActivity.releaseSuccess()
    }
}