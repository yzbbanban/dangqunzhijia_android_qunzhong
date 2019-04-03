package com.haidie.dangqun.mvp.contract.main

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.RegisterData

/**
 * Create by   Administrator
 *      on     2018/09/07 14:41
 * description
 */
class RegisterContract {
    interface View : IBaseView {
        fun setSendSMSData()
        fun setVerificationCodeData(msg: String, errorCode: Int)
        fun setRegisterData(registerData: RegisterData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View> {
        fun getSendSMSData(mobile: String, event: String)
        fun getRegisterData(event: String,mobile: String, nickname: String?, group_id: Int?, password: String,  captcha: String)
    }
}