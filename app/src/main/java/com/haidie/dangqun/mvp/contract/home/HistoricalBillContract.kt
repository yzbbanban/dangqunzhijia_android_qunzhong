package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.HistoricalBillData

/**
 * Create by   Administrator
 *      on     2018/08/30 16:07
 * description
 */
class HistoricalBillContract {
    interface View : IBaseView{
        fun setHistoricalBillData(list: List<HistoricalBillData.HistoricalBillItemData>)
        fun showError(msg : String,errorCode : Int)
        fun showRefreshEvent()
    }

    interface Presenter : IPresenter<View>{
        fun getHistoricalBillData(uid : Int,year : Int?,month : String?,page : Int,size : Int,token : String)
    }
}