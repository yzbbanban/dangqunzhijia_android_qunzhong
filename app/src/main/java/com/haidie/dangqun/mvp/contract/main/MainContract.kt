package com.haidie.dangqun.mvp.contract.main

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/09/13 13:25
 * description
 */
class MainContract {
    interface View : IBaseView{
        fun switchToBusiness()
        fun initXG()
    }

    interface Presenter : IPresenter<View>{
        fun getXGData( admin_id: Int,  device_token: String,token: String)
    }
}