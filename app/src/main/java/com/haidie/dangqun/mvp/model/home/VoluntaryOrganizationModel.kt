package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.VoluntaryOrganizationListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/11 15:30
 * description
 */
class VoluntaryOrganizationModel {
    fun getVoluntaryOrganizationListData( uid: Int, page: Int,  size: Int,  token: String) : Observable<BaseResponse<VoluntaryOrganizationListData>>{
        return RetrofitManager.service.getVoluntaryOrganizationListData(uid, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }
}