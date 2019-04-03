package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.BillDetailData
import com.haidie.dangqun.mvp.model.bean.PayResultData
import com.haidie.dangqun.mvp.model.bean.PrepaidOrderData

/**
 * Create by   Administrator
 *      on     2018/08/29 16:26
 * description
 */
class BillDetailContract {
    interface View : IBaseView{
        fun setBillDetailData(billDetailData: BillDetailData)
        fun showError(msg : String,errorCode : Int)
        fun setPrepaidOrderData(prepaidOrderDate: PrepaidOrderData)
        fun showPayError(msg : String,errorCode : Int)
        fun showPayEvent(isSuccess : Boolean)
        fun setPayResult(payResultData: PayResultData)
    }

    interface Presenter : IPresenter<View>{
        fun getBillDetailData(uid: Int,id: Int,token: String)

        fun getPrepaidOrderData(uid: Int, token: String, orderNo: String, price: String, body: String)

        fun getPayResult( uid: Int,  token: String,  orderNo: String)
    }
}