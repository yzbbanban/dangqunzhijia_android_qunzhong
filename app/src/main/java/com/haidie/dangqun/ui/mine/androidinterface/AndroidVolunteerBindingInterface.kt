package com.haidie.dangqun.ui.mine.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.dangqun.ui.mine.activity.VolunteerBindingActivity

/**
 * Create by   Administrator
 *      on     2018/09/22 11:56
 * description
 */
class AndroidVolunteerBindingInterface(activity: VolunteerBindingActivity) {
    private val volunteerBindingActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        volunteerBindingActivity.isBack(isBack)
    }
    @JavascriptInterface
    fun toPointsMallList(){
        volunteerBindingActivity.toPointsMallList()
    }
    @JavascriptInterface
    fun toVolunteerActivitiesList(){
        volunteerBindingActivity.toVolunteerActivitiesList()
    }
    @JavascriptInterface
    fun bindingSuccess(isSuccess : Boolean){
        volunteerBindingActivity.bindingSuccess(isSuccess)
    }
}