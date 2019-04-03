package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ExchangeDataListData

/**
 * Create by   Administrator
 *      on     2018/09/13 15:55
 * description
 */
class ExchangeDataListContract {
    interface View : IBaseView{
        fun setMyExchangeDataListData(exchangeDataListData: ExchangeDataListData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getMyExchangeDataListData( uid: Int,  page: Int, size: Int, token: String)
    }
}