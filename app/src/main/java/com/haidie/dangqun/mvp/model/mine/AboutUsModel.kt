package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.CheckVersionData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/18 08:55
 * description
 */
class AboutUsModel {
    fun getCheckVersionData(): Observable<BaseResponse<CheckVersionData>> {
        return RetrofitManager.service.getCheckVersionData()
                .compose(SchedulerUtils.ioToMain())
    }
}