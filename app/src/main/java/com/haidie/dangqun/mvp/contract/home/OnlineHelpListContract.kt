package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.OnlineHelpListData

/**
 * Create by   Administrator
 *      on     2018/09/12 15:47
 * description
 */
class OnlineHelpListContract {
    interface View : IBaseView{
        fun setOnlineHelpListData(onlineHelpListData: OnlineHelpListData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getOnlineHelpListData( uid: Int,  page: Int,size: Int, id: Int,  token: String)
    }
}