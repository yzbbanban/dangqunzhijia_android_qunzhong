package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.VolunteerActivitiesDetailData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/11 13:36
 * description
 */
class VolunteerActivitiesDetailModel {
    fun getVolunteerActivitiesDetailData( uid: Int,  id: Int, token: String): Observable<BaseResponse<List<VolunteerActivitiesDetailData>>>{
        return RetrofitManager.service.getVolunteerActivitiesDetailData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getAddActivityData( uid: Int, id: Int, token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getAddActivityData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getSignInData( uid: Int,  id: Int,  token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getSignInData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getVolunteerActivitiesSignInData( uid: Int,  id: Int,  token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getVolunteerActivitiesSignInData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}