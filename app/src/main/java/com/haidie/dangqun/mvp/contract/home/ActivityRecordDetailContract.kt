package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ActivityRecordDetailData

/**
 * Create by   Administrator
 *      on     2018/11/29 14:42
 * description
 */
class ActivityRecordDetailContract {
    interface View : IBaseView{
        fun setActivityRecordDetailData(activityRecordDetailData: ActivityRecordDetailData)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getActivityRecordDetailData(uid: Int,  token: String,id: Int)
    }
}