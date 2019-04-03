package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.HalfPastFourClassData

/**
 * Create by   Administrator
 *      on     2018/12/11 13:37
 * description
 */
class HalfPastFourClassContract {
    interface View : IBaseView{
        fun setHalfPastFourClassData(halfPastFourClassData: HalfPastFourClassData)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getHalfPastFourClassData(uid: Int, token: String, page: Int, size: Int, type: Int)
    }
}