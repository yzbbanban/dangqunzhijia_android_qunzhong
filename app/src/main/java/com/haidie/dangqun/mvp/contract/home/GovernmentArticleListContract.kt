package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.GovernmentArticleListData

/**
 * Create by   Administrator
 *      on     2018/09/13 13:59
 * description
 */
class GovernmentArticleListContract {
    interface View : IBaseView{
        fun setGovernmentArticleListData(governmentArticleListData: GovernmentArticleListData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getGovernmentArticleListData(uid: Int,  page: Int, size: Int,cid: Int,  token: String,type: Int?)
    }
}