package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.BoundPaymentAccountListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/11/27 14:19
 * description
 */
class LifePaymentModel {
    fun getBoundPaymentAccountListData( uid: Int,  token: String): Observable<BaseResponse<ArrayList<BoundPaymentAccountListData>>>{
        return RetrofitManager.service.getBoundPaymentAccountListData(uid, token)
                .compose(SchedulerUtils.ioToMain())
    }

}