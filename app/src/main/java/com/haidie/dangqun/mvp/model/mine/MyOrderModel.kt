package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.MyOrderListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/13 17:26
 * description
 */
class MyOrderModel {
    fun getMyOrderListData( uid: Int,  page: Int,  size: Int, id: Int,  token: String): Observable<BaseResponse<MyOrderListData>>{
        return RetrofitManager.service.getMyOrderListData(uid, page, size, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}