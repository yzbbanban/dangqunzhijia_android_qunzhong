package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.MyVolunteerActivitiesListData
import com.haidie.dangqun.mvp.model.bean.VolunteerActivitiesListData

/**
 * Create by   Administrator
 *      on     2018/09/11 11:26
 * description
 */
class VolunteerActivitiesListContract {
    interface View : IBaseView{
        fun setVolunteerActivitiesListData(volunteerActivitiesListData: VolunteerActivitiesListData)
        fun setMyVolunteerActivitiesListData(myVolunteerActivitiesListData: MyVolunteerActivitiesListData)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getVolunteerActivitiesListData( uid: Int,  groupid: Int?,  page: Int, size: Int,  type: Int,status: Int, token: String)
        fun getMyVolunteerActivitiesListData( uid: Int,  token: String, page: Int,  size: Int)
    }
}