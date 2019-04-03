package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.VoluntaryOrganizationDetailData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/11 16:03
 * description
 */
class VoluntaryOrganizationDetailModel {
    fun getVoluntaryOrganizationDetailData( uid: Int, id: Int, token: String) : Observable<BaseResponse<VoluntaryOrganizationDetailData>>{
        return RetrofitManager.service.getVoluntaryOrganizationDetailData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}