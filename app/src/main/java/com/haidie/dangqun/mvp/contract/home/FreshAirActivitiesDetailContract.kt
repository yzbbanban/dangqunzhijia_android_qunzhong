package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.FreshAirActivitiesDetailData

/**
 * Create by   Administrator
 *      on     2018/12/10 14:56
 * description
 */
class FreshAirActivitiesDetailContract {
    interface View : IBaseView{
        fun setFreshAirActivitiesDetailData(freshAirActivitiesDetailData: FreshAirActivitiesDetailData)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getFreshAirActivitiesDetailData(user_id: Int, token: String,
                                            article_id: Int)
    }
}