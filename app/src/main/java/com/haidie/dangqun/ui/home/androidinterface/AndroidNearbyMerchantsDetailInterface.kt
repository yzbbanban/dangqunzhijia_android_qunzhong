package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.NearbyMerchantsDetailActivity

/**
 * Create by   Administrator
 *      on     2018/09/22 11:56
 * description
 */
class AndroidNearbyMerchantsDetailInterface(activity: NearbyMerchantsDetailActivity) {
    private val nearbyMerchantsDetailActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        nearbyMerchantsDetailActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun toEvaluationList(url: String){
        nearbyMerchantsDetailActivity.toEvaluationList(url)
    }
    @JavascriptInterface
    fun toEvaluation(url: String){
        nearbyMerchantsDetailActivity.toEvaluation(url)
    }
    @JavascriptInterface
    fun toPicturePreview(urls: Array<String>){
        nearbyMerchantsDetailActivity.toPicturePreview(urls)
    }
}