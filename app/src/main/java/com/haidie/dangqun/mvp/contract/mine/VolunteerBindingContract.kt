package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.VolunteerInfoData

/**
 * Create by   Administrator
 *      on     2018/09/17 14:19
 * description
 */
class VolunteerBindingContract {
    interface View : IBaseView{
        fun setVolunteerInfoData(volunteerInfoData: VolunteerInfoData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getVolunteerInfoData(uid: Int, token: String)
    }
}