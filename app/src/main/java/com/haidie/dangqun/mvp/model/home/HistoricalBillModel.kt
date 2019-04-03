package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.HistoricalBillData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/08/30 16:10
 * description
 */
class HistoricalBillModel {
    fun getHistoricalBillData(uid: Int, year: Int?, month: String?, page: Int, size: Int, token: String) : Observable<BaseResponse<HistoricalBillData>>{
        return RetrofitManager.service.getHistoricalBillData(uid, year, month, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }
}