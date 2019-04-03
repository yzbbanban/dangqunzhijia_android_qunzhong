package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.MyVolunteerActivitiesListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/11 11:29
 * description
 */
class VolunteerActivitiesListModel {
    fun getVolunteerActivitiesListData(uid: Int, groupid: Int?, page: Int, size: Int, type: Int, status: Int, token: String): Observable<BaseResponse<MyVolunteerActivitiesListData>> {
        return RetrofitManager.service.getVolunteerActivitiesListData(uid, groupid, page, size, type, status, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getMyVolunteerActivitiesListData(uid: Int, token: String, page: Int, size: Int): Observable<BaseResponse<MyVolunteerActivitiesListData>> {
        return RetrofitManager.service.getMyVolunteerActivitiesListData(uid, token, page, size)
                .compose(SchedulerUtils.ioToMain())
    }
}