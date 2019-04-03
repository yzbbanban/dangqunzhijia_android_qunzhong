package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/09/11 18:29
 * description
 */
class VolunteerApplicationContract {
    interface View : IBaseView{
        fun setVolunteerApplicationData( isSuccess : Boolean,msg : String)
    }

    interface Presenter : IPresenter<View>{
        fun getVolunteerApplicationData( uid: Int,  group_id: Int, username: String,gender: Int,  phone: String,  nation: String, birthday: String,
                                         face: Int, study: Int, identity: String,  company: String, address: String,  hobby: String,
                                         skill: String,  experience: String, token: String)
    }
}