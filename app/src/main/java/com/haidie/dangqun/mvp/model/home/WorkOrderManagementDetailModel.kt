package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.HistoryReplayData
import com.haidie.dangqun.mvp.model.bean.OrderInfoData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/08 11:29
 * description
 */
class WorkOrderManagementDetailModel {
    fun getOrderInfoData(uid: Int, id: Int, token: String): Observable<BaseResponse<OrderInfoData>> {
        return RetrofitManager.service.getOrderInfoData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getHistoryReplayData(uid: Int, id: Int, page: Int, size: Int, token: String): Observable<BaseResponse<HistoryReplayData>> {
        return RetrofitManager.service.getHistoryReplayData(uid, id, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getToReplayData( uid: Int,  id: Int,  title: String,  content: String, token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getToReplayData(uid, id, title, content, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getEditStatusData( uid: Int, id: Int, status: Int, token: String) : Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getEditStatusData(uid, id, status, token)
                .compose(SchedulerUtils.ioToMain())
    }
}