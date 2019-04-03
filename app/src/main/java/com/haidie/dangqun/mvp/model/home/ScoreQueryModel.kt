package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.ScoreQueryData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/11/29 09:25
 * description
 */
class ScoreQueryModel {
    fun getScoreQueryData( uid: Int, token: String, username: String,  year: String,
                           clazz: String,  type: Int): Observable<BaseResponse<ScoreQueryData>>{
        return RetrofitManager.service.getScoreQueryData(uid, token, username, year, clazz, type)
                .compose(SchedulerUtils.ioToMain())
    }

}