package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.IReleasedData

/**
 * Create by   Administrator
 *      on     2018/09/14 15:25
 * description
 */
class IReleasedContract {
    interface View : IBaseView{
        fun setIReleasedData(iReleasedData: IReleasedData)
        fun showError(msg : String,errorCode : Int)
        fun showRefreshEvent()
    }

    interface Presenter : IPresenter<View>{
        fun getIReleasedData( uid: Int,  page: Int,  size: Int,token: String)
    }
}