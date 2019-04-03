package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ServiceListData

/**
 * Create by   Administrator
 *      on     2018/09/10 11:29
 * description
 */
class ServiceListContract {
    interface View : IBaseView{
        fun setServiceListData(serviceListData: ServiceListData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getServiceListData(id: String)
    }
}