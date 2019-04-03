package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.OrderListData

/**
 * Create by   Administrator
 *      on     2018/09/08 10:05
 * description
 */
class WorkOrderManagementContract {
    interface View : IBaseView{
        fun setOrderListData(orderListData: OrderListData)
        fun showError(msg : String,errorCode : Int)
        fun showRefreshEvent()
    }

    interface Presenter : IPresenter<View>{
        fun getOrderListData( uid: Int,  page: Int,  size: Int,id: Int,  token: String)
    }
}