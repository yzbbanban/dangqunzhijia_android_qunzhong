package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.NearbyMerchantsListActivity

/**
 * Create by   Administrator
 *      on     2018/09/22 11:56
 * description
 */
class AndroidNearbyMerchantsListInterface(activity: NearbyMerchantsListActivity) {
    private val nearbyMerchantsListActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        nearbyMerchantsListActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun toNearbyMerchantsDetail(url: String){
        nearbyMerchantsListActivity.toNearbyMerchantsDetail(url)
    }
}