package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.MyOrderDetailData

/**
 * Create by   Administrator
 *      on     2018/09/14 14:19
 * description
 */
class MyOrderDetailContract {
    interface View : IBaseView{
        fun setMyOrderDetailData(myOrderDetailData: MyOrderDetailData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getMyOrderDetailData( uid: Int, id: Int,  token: String)
    }
}