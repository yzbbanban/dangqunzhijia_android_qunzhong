package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.VoluntaryOrganizationListData

/**
 * Create by   Administrator
 *      on     2018/09/11 15:29
 * description
 */
class VoluntaryOrganizationContract {
    interface View : IBaseView{
        fun setVoluntaryOrganizationListData(voluntaryOrganizationListData: VoluntaryOrganizationListData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getVoluntaryOrganizationListData( uid: Int, page: Int,  size: Int,  token: String)
    }
}