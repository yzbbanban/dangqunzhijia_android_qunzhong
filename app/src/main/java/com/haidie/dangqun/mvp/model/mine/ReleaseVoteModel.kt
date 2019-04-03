package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/17 14:20
 * description
 */
class ReleaseVoteModel {
    fun getReleaseVoteData(uid: RequestBody, token: RequestBody, type: RequestBody, title: RequestBody,
                           content: RequestBody, start_time: RequestBody, end_time: RequestBody, choose: RequestBody,
                           part: MultipartBody.Part): Observable<BaseResponse<Boolean>> {
        return RetrofitManager.service.getReleaseVoteData(uid, token, type, title, content, start_time, end_time, choose, part)
                .compose(SchedulerUtils.ioToMain())
    }
}