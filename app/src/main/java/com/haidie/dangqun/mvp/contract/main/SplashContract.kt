package com.haidie.dangqun.mvp.contract.main

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.CheckVersionData

/**
 * Create by   Administrator
 *      on     2018/08/29 10:10
 * description
 */
class SplashContract {
    interface View : IBaseView {
        fun setCheckVersionData(checkVersionData: CheckVersionData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View> {
        fun getCheckVersionData()
    }
}