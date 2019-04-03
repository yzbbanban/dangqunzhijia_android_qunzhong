package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.BillDetailData
import com.haidie.dangqun.mvp.model.bean.PayResultData
import com.haidie.dangqun.mvp.model.bean.PrepaidOrderData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/08/29 16:50
 * description
 */
class BillDetailModel {
    fun getBillDetailData(uid: Int, id: Int, token: String): Observable<BaseResponse<BillDetailData>> {
        return RetrofitManager.service.getBillDetailData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getPrepaidOrderData(uid: Int, token: String, orderNo: String,
                            price: String, body: String): Observable<BaseResponse<PrepaidOrderData>> {
        return RetrofitManager.service.getPrepaidOrderData(uid, token, orderNo, price, body)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getPayResult( uid: Int,  token: String,  orderNo: String): Observable<BaseResponse<PayResultData>>{
        return RetrofitManager.service.getPayResult(uid, token, orderNo)
                .compose(SchedulerUtils.ioToMain())
    }
}