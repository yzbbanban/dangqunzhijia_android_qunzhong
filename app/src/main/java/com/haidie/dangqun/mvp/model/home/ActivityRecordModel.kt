package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.MyVolunteerActivitiesListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/11/29 14:13
 * description
 */
class ActivityRecordModel {
    fun getActivityRecordListData(uid: Int, token: String,  page: Int,
                                  size: Int,  type: Int): Observable<BaseResponse<MyVolunteerActivitiesListData>>{
        return RetrofitManager.service.getActivityRecordListData(uid, token, page, size, type)
                .compose(SchedulerUtils.ioToMain())
    }

}