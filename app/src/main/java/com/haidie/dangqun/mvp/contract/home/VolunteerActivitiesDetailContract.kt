package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.VolunteerActivitiesDetailData

/**
 * Create by   Administrator
 *      on     2018/09/11 13:26
 * description
 */
class VolunteerActivitiesDetailContract {
    interface View : IBaseView {
        fun setVolunteerActivitiesDetailData(volunteerActivitiesDetailData: VolunteerActivitiesDetailData)
        fun showError(msg: String, errorCode: Int)

        fun setAddActivityData(isSuccess: Boolean, msg: String, errorCode: Int)

        fun setSignInData(isSuccess: Boolean, msg: String)

        fun setVolunteerActivitiesSignInData(isSuccess: Boolean, msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getVolunteerActivitiesDetailData(uid: Int, id: Int, token: String)

        fun getAddActivityData(uid: Int, id: Int, token: String)

        fun getSignInData(uid: Int, id: Int, token: String)

        fun getVolunteerActivitiesSignInData(uid: Int, id: Int, token: String)

        fun getSignInOutData(user_id: Int,
                             token: String,
                             volunteer_id: String,
                             activity_id: String)
    }
}