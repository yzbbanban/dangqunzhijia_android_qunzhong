package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.OnlineHelpDetailsData
import com.haidie.dangqun.mvp.model.bean.OnlineHelpHistoryReplayData

/**
 * Create by   Administrator
 *      on     2018/09/12 18:15
 * description
 */
class OnlineHelpDetailsContract {
    interface View : IBaseView{
        fun setOnlineHelpDetailsData(onlineHelpDetailsData: OnlineHelpDetailsData)
        fun setOnlineHelpHistoryReplayData(onlineHelpHistoryReplayData: OnlineHelpHistoryReplayData)
        fun showError(msg : String,errorCode : Int)
        fun setOnlineHelpToReplayData(isSuccess : Boolean,msg : String)
    }

    interface Presenter : IPresenter<View>{
        fun getOnlineHelpDetailsData(uid: Int,  id: Int,token: String,page: Int,size: Int)
        fun getOnlineHelpToReplayData( uid: Int,  token: String, id: Int,  type: Int, content: String)
    }
}