package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.ServiceListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/10 11:30
 * description
 */
class ServiceListModel {
    fun getServiceListData(id: String): Observable<BaseResponse<ServiceListData>> {
        return RetrofitManager.service.getServiceListData(id)
                .compose(SchedulerUtils.ioToMain())
    }
}