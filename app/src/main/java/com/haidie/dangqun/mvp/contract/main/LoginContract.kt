package com.haidie.dangqun.mvp.contract.main

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.RegisterData
import com.haidie.dangqun.net.exception.ApiException


/**
 * Created by admin2
 *  on 2018/08/22  13:09
 * description
 */
interface LoginContract {
    interface View : IBaseView {
        fun setLoginData(registerData: RegisterData)

        fun loginFailed(e: ApiException)

        fun autoLogin(mobile : String,password : String)
    }
    interface Presenter : IPresenter<View> {
        fun getLoginData(user: String,group_id: String, password: String, device_id: String, device_type: String)
    }
}