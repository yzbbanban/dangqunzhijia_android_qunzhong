package com.haidie.dangqun.ui.mine.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.base.BaseActivity

/**
 * Create by   Administrator
 *      on     2018/09/11 15:09
 * description  返回及跳转到详情
 */
class AndroidBackDetailInterface(activity: BaseActivity) {
    private val baseActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        baseActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun callAndroid(url: String){
        baseActivity.goToDetail(url)
    }
}