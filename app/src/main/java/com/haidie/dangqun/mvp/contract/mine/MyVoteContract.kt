package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.MyVoteListData

/**
 * Create by   Administrator
 *      on     2018/09/17 13:33
 * description
 */
class MyVoteContract {
    interface View : IBaseView{
        fun setMyVoteListData(myVoteListData: MyVoteListData)
        fun showError(msg : String,errorCode : Int)
        fun showRefreshEvent()
    }

    interface Presenter : IPresenter<View>{
        fun getMyVoteListData( uid: Int, page: Int,  size: Int, token: String)
    }
}