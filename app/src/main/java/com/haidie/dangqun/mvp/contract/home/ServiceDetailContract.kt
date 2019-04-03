package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ServiceDetailItemData

/**
 * Create by   Administrator
 *      on     2018/09/10 13:12
 * description
 */
class ServiceDetailContract {
    interface View : IBaseView{
        fun setServiceDetailData(list: List<ServiceDetailItemData>)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getServiceDetailData(id: String)
    }
}