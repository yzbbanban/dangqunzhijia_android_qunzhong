package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.FreshAirActivitiesDetailData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/12/10 15:05
 * description
 */
class FreshAirActivitiesDetailModel {
    fun getFreshAirActivitiesDetailData(user_id: Int, token: String,
                                         article_id: Int): Observable<BaseResponse<FreshAirActivitiesDetailData>>{
        return RetrofitManager.service.getFreshAirActivitiesDetailData(user_id, token, article_id)
                .compose(SchedulerUtils.ioToMain())
    }

}