package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.HalfPastFourClassData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/12/11 13:36
 * description
 */
class HalfPastFourClassModel {
    fun getHalfPastFourClassData(uid: Int, token: String, page: Int, size: Int, type: Int): Observable<BaseResponse<HalfPastFourClassData>> {
        return RetrofitManager.service.getHalfPastFourClassData(uid, token, page, size, type)
                .compose(SchedulerUtils.ioToMain())
    }
}