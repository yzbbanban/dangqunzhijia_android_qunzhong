package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.LivingPaymentData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/08/29 11:21
 * description
 */
class LivingPaymentModel {
    fun getLivingPaymentData( uid: Int, token: String,  page: Int,
                              size: Int, id: Int): Observable<BaseResponse<LivingPaymentData>>{
        return RetrofitManager.service.getLivingPaymentData(uid, token, page, size, id)
                .compose(SchedulerUtils.ioToMain())
    }
}