package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.NearbyMerchantsActivity

/**
 * Create by   Administrator
 *      on     2018/09/22 11:56
 * description
 */
class AndroidNearbyMerchantsInterface(activity: NearbyMerchantsActivity) {
    private val nearbyMerchantsActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        nearbyMerchantsActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun toNearbyMerchantsList(url: String){
        nearbyMerchantsActivity.toNearbyMerchantsList(url)
    }
    @JavascriptInterface
    fun toNearbyMerchantsDetail(url: String){
        nearbyMerchantsActivity.toNearbyMerchantsDetail(url)
    }
}