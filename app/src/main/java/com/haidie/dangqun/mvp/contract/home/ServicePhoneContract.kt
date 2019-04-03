package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ServicePhoneData

/**
 * Create by   Administrator
 *      on     2019/01/05 10:52
 * description
 */
class ServicePhoneContract {
    interface View : IBaseView{
        fun setServicePhoneData(servicePhoneData: ArrayList<ServicePhoneData> )
    }
    interface Presenter :IPresenter<View>{
        fun getServicePhoneData()
    }
}