package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/09/10 13:45
 * description
 */
class OnlineOrderContract {
    interface View : IBaseView{
        fun setCreateOrderData( isSuccess : Boolean,msg : String)
    }

    interface Presenter : IPresenter<View>{
        fun getCreateOrderData( uid: Int,  sid: Int, username: String, phone: String, content: String,  time: String,  address: String,
                                token: String)
    }
}