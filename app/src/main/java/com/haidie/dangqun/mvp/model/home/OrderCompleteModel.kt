package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/10 14:23
 * description
 */
class OrderCompleteModel {
    fun getSubmitEvaluationData( uid: Int,  id: Int, rank: Int,  content: String,token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getSubmitEvaluationData(uid, id, rank, content, token)
                .compose(SchedulerUtils.ioToMain())
    }
}