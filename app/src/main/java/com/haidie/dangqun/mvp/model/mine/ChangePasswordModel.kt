package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/08/28 11:00
 * description
 */
class ChangePasswordModel {
    fun getChangePasswordData(uid: Int, token: String, oldpwd: String, newpwd: String, repwd: String): Observable<BaseResponse<String>> {
        return RetrofitManager.service.getChangePasswordData(uid, token, oldpwd, newpwd, repwd)
                .compose(SchedulerUtils.ioToMain())
    }
}