package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.MyOrderDetailData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/14 14:20
 * description
 */
class MyOrderDetailModel {
    fun getMyOrderDetailData( uid: Int, id: Int,  token: String): Observable<BaseResponse<List<MyOrderDetailData>>>{
        return RetrofitManager.service.getMyOrderDetailData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}