package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.VolunteersParticipateActivitiesListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/11 14:19
 * description
 */
class VolunteersParticipateActivitiesModel {
    fun getVolunteersParticipateActivitiesListData(uid: Int, id: Int,  page: Int, size: Int, token: String): Observable<BaseResponse<VolunteersParticipateActivitiesListData>>{
        return RetrofitManager.service.getVolunteersParticipateActivitiesListData(uid, id, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }
}