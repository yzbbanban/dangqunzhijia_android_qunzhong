package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.VolunteersParticipateActivitiesListData

/**
 * Create by   Administrator
 *      on     2018/09/11 14:16
 * description
 */
class VolunteersParticipateActivitiesContract {
    interface View : IBaseView{
        fun setVolunteersParticipateActivitiesListData(volunteersParticipateActivitiesListData: VolunteersParticipateActivitiesListData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getVolunteersParticipateActivitiesListData(uid: Int, id: Int,  page: Int, size: Int, token: String)
    }
}