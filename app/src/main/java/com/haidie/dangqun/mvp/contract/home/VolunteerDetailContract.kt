package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.VolunteerDetailData

/**
 * Create by   Administrator
 *      on     2018/09/12 09:20
 * description
 */
class VolunteerDetailContract {
    interface View : IBaseView{
        fun setVolunteerDetailData(volunteerDetailData: VolunteerDetailData)
        fun showError(msg : String,errorCode : Int)
        fun setEditAdministratorStatusData(isSuccess : Boolean,msg : String)
        fun setEditPendingVolunteerChangeData(isSuccess : Boolean,msg : String)
    }

    interface Presenter : IPresenter<View>{
        fun getVolunteerDetailData( uid: Int, id: Int,  token: String)
        fun getEditAdministratorStatusData(uid: Int, id: Int,status: Int, token: String)
        fun getEditPendingVolunteerChangeData( uid: Int, id: Int,  status: Int, token: String)
    }
}