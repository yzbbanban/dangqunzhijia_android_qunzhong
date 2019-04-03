package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.ServiceCategoryData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/10 10:47
 * description
 */
class ServiceCategoryModel {
    fun getServiceCategoryData( id: String): Observable<BaseResponse<ServiceCategoryData>>{
        return RetrofitManager.service.getServiceCategoryData(id)
                .compose(SchedulerUtils.ioToMain())
    }
}