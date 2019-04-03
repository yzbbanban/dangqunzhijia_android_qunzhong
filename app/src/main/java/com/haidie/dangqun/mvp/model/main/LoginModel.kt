package com.haidie.dangqun.mvp.model.main

import com.haidie.dangqun.mvp.model.bean.RegisterData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by admin2
 *  on 2018/08/22  13:11
 * description
 */
class LoginModel {
    fun getLoginData(username: String,  group_id: String,
                     password: String, device_id: String, device_type: String): Observable<BaseResponse<RegisterData>> {
        return RetrofitManager.service.getLoginData(username,  group_id, password, device_id, device_type)
                .compose(SchedulerUtils.ioToMain())
    }
}