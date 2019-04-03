package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.CheckVersionData

/**
 * Create by   Administrator
 *      on     2018/09/18 08:53
 * description
 */
class AboutUsContract {
    interface View : IBaseView{
        fun setCheckVersionData(checkVersionData: CheckVersionData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getCheckVersionData()
    }
}