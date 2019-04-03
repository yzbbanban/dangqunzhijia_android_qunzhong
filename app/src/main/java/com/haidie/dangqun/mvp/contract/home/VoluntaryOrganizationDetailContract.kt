package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.VoluntaryOrganizationDetailData

/**
 * Create by   Administrator
 *      on     2018/09/11 16:00
 * description
 */
class VoluntaryOrganizationDetailContract {
    interface View : IBaseView{
        fun setVoluntaryOrganizationDetailData(voluntaryOrganizationDetailData: VoluntaryOrganizationDetailData)
        fun showError(msg : String,errorCode : Int)
        fun showRefreshEvent()
    }

    interface Presenter : IPresenter<View>{
        fun getVoluntaryOrganizationDetailData( uid: Int, id: Int, token: String)
    }
}