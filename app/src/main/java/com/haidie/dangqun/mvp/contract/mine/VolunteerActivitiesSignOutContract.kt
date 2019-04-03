package com.haidie.dangqun.mvp.contract.mine

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/11/06 11:47
 * description
 */
class VolunteerActivitiesSignOutContract {
    interface View : IBaseView{
        fun setVolunteerActivitiesSignOutData(isSuccess : Boolean,msg : String)
    }
    interface Presenter : IPresenter<View>{
        fun getVolunteerActivitiesSignOutData(uid: RequestBody, id: RequestBody, token: RequestBody,content: RequestBody,
                                              part: MultipartBody.Part)
    }
}