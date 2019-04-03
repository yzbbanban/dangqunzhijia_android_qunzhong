package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.ServiceCategoryData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/08 08:39
 * description
 */
class SubmitWorkOrderModel {
    fun getServiceCategoryData(id: String): Observable<BaseResponse<ServiceCategoryData>>{
        return RetrofitManager.service.getServiceCategoryData(id)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getToWorkOrderData(uid: RequestBody,  cid: RequestBody, title: RequestBody,  content: RequestBody,
                            token: RequestBody,  parts: List<MultipartBody.Part>): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getToWorkOrderData(uid, cid, title, content, token, parts)
                .compose(SchedulerUtils.ioToMain())
    }
}