package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.BoundPaymentAccountListData

/**
 * Create by   Administrator
 *      on     2018/11/27 14:12
 * description
 */
class LifePaymentContract {
    interface View : IBaseView {
        fun setBoundPaymentAccountListData(boundPaymentAccountListData: ArrayList<BoundPaymentAccountListData>?)
        fun showError(msg: String, errorCode: Int)
        fun showRefreshEvent()
    }
    interface Presenter : IPresenter<View> {
        fun getBoundPaymentAccountListData( uid: Int,  token: String)
    }
}