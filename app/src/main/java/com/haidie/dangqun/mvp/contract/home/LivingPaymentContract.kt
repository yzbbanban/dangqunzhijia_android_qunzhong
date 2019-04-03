package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.LivingPaymentData

/**
 * Create by   Administrator
 *      on     2018/08/29 11:17
 * description
 */
class LivingPaymentContract {
    interface View : IBaseView{
        fun setLivingPaymentData(list: List<LivingPaymentData.LivingPaymentItemData>)
        fun showError(msg : String,errorCode : Int)
        fun showRefreshEvent()
    }

    interface Presenter : IPresenter<View>{
        fun getLivingPaymentData( uid: Int, token: String,  page: Int, size: Int, id: Int)
    }
}