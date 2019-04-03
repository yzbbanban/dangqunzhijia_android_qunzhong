package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.FreshAirActivitiesData

/**
 * Create by   Administrator
 *      on     2018/12/10 13:59
 * description
 */
class FreshAirActivitiesContract {
    interface View : IBaseView{
        fun setFreshAirActivitiesData(freshAirActivitiesData: FreshAirActivitiesData)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getFreshAirActivitiesData(user_id: Int, token: String,page: Int,  size: Int,  module_type: Int,
                                      is_register_copy: Int)
    }
}