package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.IParticipatedData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/14 15:27
 * description
 */
class IParticipatedModel {
    fun getIParticipatedData( uid: Int,  page: Int,  size: Int, id: Int?,  token: String): Observable<BaseResponse<IParticipatedData>>{
        return RetrofitManager.service.getIParticipatedData(uid, page, size, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}