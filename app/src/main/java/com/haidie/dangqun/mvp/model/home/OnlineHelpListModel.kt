package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.OnlineHelpListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/12 15:49
 * description
 */
class OnlineHelpListModel {
    fun getOnlineHelpListData( uid: Int,  page: Int,size: Int, id: Int,  token: String): Observable<BaseResponse<OnlineHelpListData>>{
        return RetrofitManager.service.getOnlineHelpListData(uid, page, size, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}