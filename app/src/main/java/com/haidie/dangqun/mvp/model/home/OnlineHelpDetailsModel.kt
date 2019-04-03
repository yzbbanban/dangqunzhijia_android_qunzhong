package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.OnlineHelpDetailsData
import com.haidie.dangqun.mvp.model.bean.OnlineHelpHistoryReplayData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/12 18:16
 * description
 */
class OnlineHelpDetailsModel {
    fun getOnlineHelpDetailsData(uid: Int,  id: Int,token: String): Observable<BaseResponse<OnlineHelpDetailsData>>{
        return RetrofitManager.service.getOnlineHelpDetailsData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getOnlineHelpHistoryReplayData( uid: Int,  id: Int,  page: Int,size: Int, token: String): Observable<BaseResponse<OnlineHelpHistoryReplayData>>{
        return RetrofitManager.service.getOnlineHelpHistoryReplayData(uid, id, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getOnlineHelpToReplayData( uid: Int,  token: String, id: Int,  type: Int, content: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getOnlineHelpToReplayData(uid, token, id, type, content)
                .compose(SchedulerUtils.ioToMain())
    }
}