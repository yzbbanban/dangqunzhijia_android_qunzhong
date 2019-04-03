package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ServiceCategoryData

/**
 * Create by   Administrator
 *      on     2018/09/10 10:46
 * description
 */
class ServiceCategoryContract {
    interface View : IBaseView{
        fun setServiceCategoryData(serviceCategoryData: ServiceCategoryData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getServiceCategoryData( id: String)
    }
}