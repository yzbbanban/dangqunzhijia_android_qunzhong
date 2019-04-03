package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.OnlineHelpListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/10/23 14:23
 * description
 */
class MyHelpListModel {
    fun getOnlineHelpData( uid: Int,  page: Int,  size: Int,  id: Int, check_status: Int,  status: Int, token: String): Observable<BaseResponse<OnlineHelpListData>>{
        return RetrofitManager.service.getOnlineHelpData(uid, page, size, id, check_status, status, token)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getOnlineHelpSubmitAgainData(uid: Int,  token: String, id: Int, status: Int): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getOnlineHelpSubmitAgainData(uid, token, id, status)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getOnlineHelpDeleteData( uid: Int,  token: String, id: Int, status: Int): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getOnlineHelpDeleteData(uid, token, id, status)
                .compose(SchedulerUtils.ioToMain())
    }
}