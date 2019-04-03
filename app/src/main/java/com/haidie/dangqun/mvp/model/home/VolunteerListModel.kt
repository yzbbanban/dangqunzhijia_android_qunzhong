package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.VolunteerListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/11 16:52
 * description
 */
class VolunteerListModel {
    fun getVolunteerListData( uid: Int,  group_id: Int, type: Int,  search: String, page: Int, size: Int, token: String): Observable<BaseResponse<VolunteerListData>>{
        return RetrofitManager.service.getVolunteerListData(uid, group_id, type, search, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }
}