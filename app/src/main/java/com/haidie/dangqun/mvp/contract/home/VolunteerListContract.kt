package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.VolunteerListData

/**
 * Create by   Administrator
 *      on     2018/09/11 16:51
 * description
 */
class VolunteerListContract {
    interface View : IBaseView{
        fun setVolunteerListData(volunteerListData: VolunteerListData)
        fun showError(msg : String,errorCode : Int)
        fun showRefreshEvent()
    }

    interface Presenter : IPresenter<View>{
        fun getVolunteerListData( uid: Int,  group_id: Int, type: Int,  search: String, page: Int, size: Int, token: String)
    }
}