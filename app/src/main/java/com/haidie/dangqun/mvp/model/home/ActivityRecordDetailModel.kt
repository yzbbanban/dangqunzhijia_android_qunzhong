package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.ActivityRecordDetailData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/11/29 14:43
 * description
 */
class ActivityRecordDetailModel {
    fun getActivityRecordDetailData(uid: Int,  token: String,id: Int): Observable<BaseResponse<ActivityRecordDetailData>>{
        return RetrofitManager.service.getActivityRecordDetailData(uid, token, id)
                .compose(SchedulerUtils.ioToMain())
    }
}