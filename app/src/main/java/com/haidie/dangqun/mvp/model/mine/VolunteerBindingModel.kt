package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.VolunteerInfoData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/10/22 14:15
 * description
 */
class VolunteerBindingModel {
    fun getVolunteerInfoData( uid: Int,  token: String): Observable<BaseResponse<VolunteerInfoData>>{
        return RetrofitManager.service.getVolunteerInfoData(uid, token)
                .compose(SchedulerUtils.ioToMain())
    }
}