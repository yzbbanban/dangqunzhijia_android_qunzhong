package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/08 15:50
 * description
 */
class WorkOrderEvaluationModel {

    fun getEditStatusData( uid: Int, id: Int, status: Int, token: String) : Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getEditStatusData(uid, id, status, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getToCommentData(uid: Int, id: Int, rank: Int, content: String, token: String): Observable<BaseResponse<Boolean>> {
        return RetrofitManager.service.getToCommentData(uid, id, rank, content, token)
                .compose(SchedulerUtils.ioToMain())
    }

}