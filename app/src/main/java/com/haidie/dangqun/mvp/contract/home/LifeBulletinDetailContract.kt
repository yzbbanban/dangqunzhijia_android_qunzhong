package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ActivityDetailData
import com.haidie.dangqun.mvp.model.bean.ArticleDetailData

/**
 * Create by   Administrator
 *      on     2018/09/10 09:55
 * description
 */
class LifeBulletinDetailContract {
    interface View : IBaseView{
        fun setArticleDetailData(articleDetailDataList: List<ArticleDetailData>)
        fun showError(msg : String,errorCode : Int)

        fun setActivityDetailData(activityDetailData: ActivityDetailData)
        fun setAddActivityData(isSuccess : Boolean,msg : String)
    }

    interface Presenter : IPresenter<View>{
        fun getArticleDetailData(id: String)

        fun getActivityDetailData( id: String, uid: Int,  token: String)

        fun getAddActivityData( uid: Int, id: Int, token: String)
    }
}