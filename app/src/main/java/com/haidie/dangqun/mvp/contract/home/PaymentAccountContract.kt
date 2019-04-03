package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.BoundPaymentAccountListData

/**
 * Create by   Administrator
 *      on     2018/11/27 16:21
 * description
 */
class PaymentAccountContract {
    interface View : IBaseView {
        fun setBoundPaymentAccountListData(boundPaymentAccountListData: ArrayList<BoundPaymentAccountListData>?)
        fun showError(msg: String, errorCode: Int)
        fun setUnbindPaymentAccountResultData(isSuccess : Boolean,msg: String)
    }
    interface Presenter : IPresenter<View> {
        fun getBoundPaymentAccountListData( uid: Int,  token: String)
        fun getUnbindPaymentAccountResultData( uid: Int,  token: String,id: Int)
    }
}