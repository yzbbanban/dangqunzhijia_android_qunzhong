package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.PointsMallListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/11 09:29
 * description
 */
class PointsMallListModel {
    fun getPointsMallListData( uid: Int, type: Int,  page: Int, size: Int,  token: String): Observable<BaseResponse<PointsMallListData>>{
        return RetrofitManager.service.getPointsMallListData(uid, type, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getExchangeData( uid: Int, id: Int, token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getExchangeData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}