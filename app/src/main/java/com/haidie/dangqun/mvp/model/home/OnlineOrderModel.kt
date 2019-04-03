package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/10 13:46
 * description
 */
class OnlineOrderModel {
    fun getCreateOrderData( uid: Int,  sid: Int, username: String, phone: String, content: String,  time: String,  address: String,
                           token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getCreateOrderData(uid, sid, username, phone, content, time, address, token)
                .compose(SchedulerUtils.ioToMain())
    }
}