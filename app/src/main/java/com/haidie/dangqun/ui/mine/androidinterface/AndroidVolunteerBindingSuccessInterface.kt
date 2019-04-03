package com.haidie.dangqun.ui.mine.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.mine.activity.VolunteerBindingSuccessActivity

/**
 * Create by   Administrator
 *      on     2018/09/22 11:56
 * description
 */
class AndroidVolunteerBindingSuccessInterface(activity: VolunteerBindingSuccessActivity) {
    private val volunteerBindingSuccessActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        volunteerBindingSuccessActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun toPointsMallList(){
        volunteerBindingSuccessActivity.toPointsMallList()
    }
    @JavascriptInterface
    fun toVolunteerActivitiesList(){
        volunteerBindingSuccessActivity.toVolunteerActivitiesList()
    }

}