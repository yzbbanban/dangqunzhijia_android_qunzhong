package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/11/06 11:51
 * description
 */
class VolunteerActivitiesSignOutModel {
    fun getVolunteerActivitiesSignOutData(uid: RequestBody, id: RequestBody, token: RequestBody,
                                          content: RequestBody, part: MultipartBody.Part): Observable<BaseResponse<Boolean>> {
        return RetrofitManager.service.getVolunteerActivitiesSignOutData(uid, id, token, content, part)
                .compose(SchedulerUtils.ioToMain())
    }

}