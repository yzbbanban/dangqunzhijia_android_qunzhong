package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ArticleListData

/**
 * Create by   Administrator
 *      on     2018/09/08 17:25
 * description
 */
class LifeBulletinContract {
    interface View : IBaseView{
        fun setArticleListData(articleListData: ArticleListData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getArticleListData( id: String)
    }
}