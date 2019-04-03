package com.haidie.dangqun.mvp.model.main

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/10 15:16
 * description
 */
class ForgotPasswordModel {
    fun getSendSMSData( mobile: String,  event: String): Observable<BaseResponse<String>>{
        return RetrofitManager.service.getSendSMSData(mobile, event)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getCheckVerificationCodeData( mobile: String,  event: String,  captcha: String) : Observable<BaseResponse<String>>{
        return RetrofitManager.service.getCheckVerificationCodeData(mobile, event, captcha)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getResetPwdData( mobile: String,  type: String,  newpwd: String,captcha: String): Observable<BaseResponse<String>>{
        return RetrofitManager.service.getResetPwdData(mobile, type, newpwd, captcha)
                .compose(SchedulerUtils.ioToMain())
    }
}