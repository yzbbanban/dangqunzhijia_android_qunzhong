package com.haidie.dangqun.mvp.model.main

import com.haidie.dangqun.mvp.model.bean.CheckVersionData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/17 17:56
 * description
 */
class SplashModel {
    fun getCheckVersionData(): Observable<BaseResponse<CheckVersionData>>{
        return RetrofitManager.service.getCheckVersionData()
                .compose(SchedulerUtils.ioToMain())
    }
}