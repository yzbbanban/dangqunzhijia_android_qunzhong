package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.CommodityClassificationListData

/**
 * Create by   Administrator
 *      on     2018/10/12 13:25
 * description
 */
class CommodityTradingContract {
    interface View : IBaseView {
        fun setCommodityClassificationListData(list: ArrayList<CommodityClassificationListData>)

        fun reloadBusiness()

        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View> {
        fun getCommodityClassificationListData(uid: Int, token: String)
    }
}