package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.MyOrderListData

/**
 * Create by   Administrator
 *      on     2018/09/13 17:26
 * description
 */
class MyOrderContract {
    interface View : IBaseView{
        fun setMyOrderListData(myOrderListData: MyOrderListData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getMyOrderListData( uid: Int,  page: Int,  size: Int, id: Int,  token: String)
    }
}