package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.HomeBannerData
import com.haidie.dangqun.mvp.model.bean.HomeNewsData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/13 08:45
 * description
 */
class HomeModel {
    fun getHomeBannerData( user_id: Int,  token: String): Observable<BaseResponse<HomeBannerData>>{
        return RetrofitManager.service.getHomeBannerData(user_id, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getHomeNewsData( user_id: Int,  token: String): Observable<BaseResponse<HomeNewsData>>{
        return RetrofitManager.service.getHomeNewsData(user_id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}