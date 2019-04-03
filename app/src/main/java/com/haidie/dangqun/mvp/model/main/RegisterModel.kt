package com.haidie.dangqun.mvp.model.main

import com.haidie.dangqun.mvp.model.bean.RegisterData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/07 14:43
 * description
 */
class RegisterModel {
    fun getSendSMSData( mobile: String,  event: String): Observable<BaseResponse<String>>{
        return RetrofitManager.service.getSendSMSData(mobile, event)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getCheckVerificationCodeData( mobile: String,  event: String,  captcha: String) : Observable<BaseResponse<String>>{
        return RetrofitManager.service.getCheckVerificationCodeData(mobile, event, captcha)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getRegisterData(mobile: String, nickname: String?, group_id: Int?,
                         password: String,  captcha: String): Observable<BaseResponse<RegisterData>>{
        return RetrofitManager.service.getRegisterData(mobile, nickname, group_id, password, captcha)
                .compose(SchedulerUtils.ioToMain())
    }
}