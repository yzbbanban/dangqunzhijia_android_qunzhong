package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.PointsMallListData

/**
 * Create by   Administrator
 *      on     2018/09/11 09:27
 * description
 */
class PointsMallListContract {
    interface View : IBaseView{
        fun setPointsMallListData(pointsMallListData: PointsMallListData)
        fun showError(msg : String,errorCode : Int)
        fun setExchangeData(isSuccess : Boolean,msg : String)
    }

    interface Presenter : IPresenter<View>{
        fun getPointsMallListData( uid: Int, type: Int,  page: Int, size: Int,  token: String)
        fun getExchangeData( uid: Int, id: Int, token: String)
    }
}