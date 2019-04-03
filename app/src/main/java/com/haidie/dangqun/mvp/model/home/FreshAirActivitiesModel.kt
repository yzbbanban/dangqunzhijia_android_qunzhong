package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.FreshAirActivitiesData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/12/10 13:59
 * description
 */
class FreshAirActivitiesModel {
    fun getFreshAirActivitiesData(user_id: Int, token: String,page: Int,  size: Int,  module_type: Int,
                                   is_register_copy: Int): Observable<BaseResponse<FreshAirActivitiesData>>{
        return RetrofitManager.service.getFreshAirActivitiesData(user_id, token, page, size, module_type, is_register_copy)
                .compose(SchedulerUtils.ioToMain())
    }

}