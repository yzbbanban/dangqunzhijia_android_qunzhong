package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.OnlineHelpListData

/**
 * Create by   Administrator
 *      on     2018/09/17 14:19
 * description
 */
class MyHelpListContract {
    interface View : IBaseView{
        fun setMyHelpListData(onlineHelpListData: OnlineHelpListData)
        fun setSubmitAgainData(isSuccess : Boolean,msg : String)
        fun setDeleteData(isSuccess : Boolean,msg : String)
        fun reloadMyHelp()
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getOnlineHelpData( uid: Int,  page: Int,  size: Int,  id: Int, check_status: Int,  status: Int, token: String)

        fun getOnlineHelpSubmitAgainData(uid: Int,  token: String, id: Int, status: Int)
        fun getOnlineHelpDeleteData( uid: Int,  token: String, id: Int, status: Int)
    }
}