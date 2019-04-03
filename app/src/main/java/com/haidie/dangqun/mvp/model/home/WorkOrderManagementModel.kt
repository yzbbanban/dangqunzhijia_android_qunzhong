package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.OrderListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/08 10:07
 * description
 */
class WorkOrderManagementModel {
    fun getOrderListData( uid: Int,  page: Int,  size: Int,id: Int,  token: String): Observable<BaseResponse<OrderListData>>{
        return RetrofitManager.service.getOrderListData(uid, page, size, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}