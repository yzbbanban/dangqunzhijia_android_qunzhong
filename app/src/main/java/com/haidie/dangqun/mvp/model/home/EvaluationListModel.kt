package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.EvaluationListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/12 12:00
 * description
 */
class EvaluationListModel {
    fun getEvaluationListData(uid: Int, category: Int, page: Int, size: Int,  token: String): Observable<BaseResponse<EvaluationListData>>{
        return RetrofitManager.service.getEvaluationListData(uid, category, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }
}