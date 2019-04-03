package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.OnlineHelpDetailsData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/10/23 14:23
 * description
 */
class MyHelpEditModel {
    fun getOnlineHelpDetailsData(uid: Int,  id: Int,token: String): Observable<BaseResponse<OnlineHelpDetailsData>> {
        return RetrofitManager.service.getOnlineHelpDetailsData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getMyHelpEditData(uid: RequestBody, token: RequestBody, id: RequestBody, type: RequestBody?, troubletype: RequestBody,
                          is_online: RequestBody, username: RequestBody, gender: RequestBody, phone: RequestBody, identity: RequestBody,
                          address: RequestBody?, title: RequestBody, content: RequestBody, pic1: RequestBody?, pic2: RequestBody?,
                          pic3: RequestBody?,  part1: MultipartBody.Part?,  part2: MultipartBody.Part?,
                           part3: MultipartBody.Part?): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getMyHelpEditData(uid, token, id, type, troubletype, is_online, username, gender, phone, identity, address, title, content,
                pic1, pic2, pic3,part1,part2,part3)
                .compose(SchedulerUtils.ioToMain())
    }
}