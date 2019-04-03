package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.HistoryReplayData
import com.haidie.dangqun.mvp.model.bean.OrderInfoData

/**
 * Create by   Administrator
 *      on     2018/09/08 11:22
 * description
 */
class WorkOrderManagementDetailContract {
    interface View : IBaseView{
        fun setOrderInfoData(orderInfoData: OrderInfoData)
        fun showError(msg : String,errorCode : Int)
        fun setHistoryReplayData(historyReplayData: HistoryReplayData)
        fun setToReplayData(isSuccess: Boolean,msg : String)
        fun setEditStatusData(isSuccess: Boolean,msg : String)
        fun showRefreshEvent()
    }
    interface Presenter : IPresenter<View>{
        fun getOrderInfoData( uid: Int,  id: Int,  token: String, page: Int,size: Int)
        fun getToReplayData( uid: Int,  id: Int,  title: String,  content: String, token: String)
        fun getEditStatusData( uid: Int, id: Int, status: Int, token: String)
    }
}