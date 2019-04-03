package com.haidie.dangqun.ui.business.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.business.fragment.BusinessFragment

/**
 * Create by   Administrator
 *      on     2018/09/03 11:16
 * description
 */
class AndroidBusinessListInterface(fragment : BusinessFragment) {
    private val businessFragment: BusinessFragment = fragment
    @JavascriptInterface
    fun callAndroid(url: String){
        //跳转到详情页面
        businessFragment.goToBusinessDetail(url)
    }

}