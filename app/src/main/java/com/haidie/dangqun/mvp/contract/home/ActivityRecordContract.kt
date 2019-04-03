package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.MyVolunteerActivitiesListData

/**
 * Create by   Administrator
 *      on     2018/11/29 14:12
 * description
 */
class ActivityRecordContract {
    interface View : IBaseView{
        fun setActivityRecordListData(myVolunteerActivitiesListData: MyVolunteerActivitiesListData)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getActivityRecordListData(uid: Int, token: String,  page: Int,
                                      size: Int,  type: Int)
    }
}