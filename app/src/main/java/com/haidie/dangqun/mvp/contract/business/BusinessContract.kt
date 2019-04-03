package com.haidie.dangqun.mvp.contract.business

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.CommodityClassificationListData

/**
 * Created by admin2
 *  on 2018/08/13  19:56
 * description
 */
interface BusinessContract {
    interface View : IBaseView {
        fun setCommodityClassificationListData(list: ArrayList<CommodityClassificationListData>)

        fun reloadBusiness()

        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View> {
        fun getCommodityClassificationListData(uid: Int, token: String)
    }
}