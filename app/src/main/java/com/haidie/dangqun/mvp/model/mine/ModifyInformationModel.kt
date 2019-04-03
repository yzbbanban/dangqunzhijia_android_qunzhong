package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ModifyInformationModel {
    fun getModifyUserInfoData(uid: RequestBody, token: RequestBody,
                              gender: RequestBody, nickname: RequestBody,
                              birthday: RequestBody, part: MultipartBody.Part?): Observable<BaseResponse<String>> {
        return RetrofitManager.service.getModifyUserInfoData(uid, token, gender,
                nickname, birthday, part)
                .compose(SchedulerUtils.ioToMain())
    }
}