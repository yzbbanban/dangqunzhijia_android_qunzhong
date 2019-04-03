package com.haidie.dangqun.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.home.activity.NearbyMerchantsEvaluationListActivity

/**
 * Create by   Administrator
 *      on     2018/09/22 11:56
 * description
 */
class AndroidNearbyMerchantsEvaluationListInterface(activity: NearbyMerchantsEvaluationListActivity) {
    private val nearbyMerchantsEvaluationListActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        nearbyMerchantsEvaluationListActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun toEvaluation(url: String){
        nearbyMerchantsEvaluationListActivity.toEvaluation(url)
    }
}