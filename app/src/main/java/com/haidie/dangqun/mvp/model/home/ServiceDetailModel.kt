package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.ServiceDetailItemData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/10 13:15
 * description
 */
class ServiceDetailModel {
    fun getServiceDetailData(id: String): Observable<BaseResponse<List<ServiceDetailItemData>>>{
        return RetrofitManager.service.getServiceDetailData(id)
                .compose(SchedulerUtils.ioToMain())
    }
}