package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/14 16:22
 * description
 */
class ReleaseActivitiesModel {
    fun getReleaseActivitiesData(uid: RequestBody, type: RequestBody, title: RequestBody, content: RequestBody, start_time: RequestBody,
                                 end_time: RequestBody, area: RequestBody, address: RequestBody, token: RequestBody,
                                 part: MultipartBody.Part): Observable<BaseResponse<Boolean>> {
        return RetrofitManager.service.getReleaseActivitiesData(uid, type, title, content, start_time, end_time, area, address, token, part)
                .compose(SchedulerUtils.ioToMain())
    }
}