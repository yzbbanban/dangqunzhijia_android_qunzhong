package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.HalfPastFourClassDetailData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/12/11 14:30
 * description
 */
class HalfPastFourClassDetailModel {
    fun getHalfPastFourClassDetailData(uid: Int,token: String,id: Int): Observable<BaseResponse<HalfPastFourClassDetailData>>{
        return RetrofitManager.service.getHalfPastFourClassDetailData(uid, token, id)
                .compose(SchedulerUtils.ioToMain())
    }
}