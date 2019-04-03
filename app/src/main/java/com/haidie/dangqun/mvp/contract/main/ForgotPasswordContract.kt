package com.haidie.dangqun.mvp.contract.main

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/09/10 15:13
 * description
 */
class ForgotPasswordContract {

    interface View : IBaseView {
        fun setSendSMSData()
        fun showError(msg: String, errorCode: Int)
        fun setResetPwdData(isSuccess: Boolean)
        fun setVerificationCodeData(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getSendSMSData(mobile: String, event: String)

        fun getResetPwdData(event: String, mobile: String, type: String, newpwd: String, captcha: String)
    }
}