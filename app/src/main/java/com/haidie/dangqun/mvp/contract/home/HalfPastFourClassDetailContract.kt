package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.HalfPastFourClassDetailData

/**
 * Create by   Administrator
 *      on     2018/12/11 14:29
 * description
 */
class HalfPastFourClassDetailContract {
    interface View : IBaseView{
        fun setHalfPastFourClassDetailData(halfPastFourClassDetailData: HalfPastFourClassDetailData)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getHalfPastFourClassDetailData(uid: Int,token: String,id: Int)
    }
}