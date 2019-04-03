package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.VolunteerDetailData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/12 09:22
 * description
 */
class VolunteerDetailModel {
    fun getVolunteerDetailData( uid: Int, id: Int,  token: String): Observable<BaseResponse<VolunteerDetailData>>{
        return RetrofitManager.service.getVolunteerDetailData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getEditAdministratorStatusData(uid: Int, id: Int,status: Int, token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getEditAdministratorStatusData(uid, id, status, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getEditPendingVolunteerChangeData( uid: Int, id: Int,  status: Int, token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getEditPendingVolunteerChangeData(uid, id, status, token)
                .compose(SchedulerUtils.ioToMain())
    }
}