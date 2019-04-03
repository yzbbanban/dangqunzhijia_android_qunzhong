package com.haidie.dangqun.ui.life.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.life.activity.LifeDetailActivity

/**
 * Created by admin2
 *  on 2018/08/21  9:23
 * description
 */
class AndroidLifeDetailInterface(activity: LifeDetailActivity) {
    private val mLifeDetailActivity: LifeDetailActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        mLifeDetailActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun callAndroid(url: String){
        mLifeDetailActivity.goToPersonalInformationArticle(url)
    }
    @JavascriptInterface
    fun toPicturePreview(urls: Array<String>){
        mLifeDetailActivity.toPicturePreview(urls)
    }
}