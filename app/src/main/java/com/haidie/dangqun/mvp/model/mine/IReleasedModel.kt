package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.IReleasedData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/14 15:27
 * description
 */
class IReleasedModel {
    fun getIReleasedData( uid: Int,  page: Int,  size: Int,token: String): Observable<BaseResponse<IReleasedData>>{
        return RetrofitManager.service.getIReleasedData(uid, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }
}