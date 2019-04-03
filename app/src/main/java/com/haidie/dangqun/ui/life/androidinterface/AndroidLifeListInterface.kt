package com.haidie.dangqun.ui.life.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.base.BaseFragment

/**
 * Created by admin2
 *  on 2018/08/21  11:39
 * description
 */
class AndroidLifeListInterface( baseFragment: BaseFragment) {
    private val lifeListFragment = baseFragment
    @JavascriptInterface
    fun callAndroid(url: String){
        //跳转到详情页面
        lifeListFragment.goToDetail(url)
    }

}