package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.MineData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by admin2
 *  on 2018/08/10  15:09
 * description
 */
class MineModel {
    fun getMineData(uid: Int,token: String) : Observable<BaseResponse<MineData>>{
        return RetrofitManager.service.getMineData(uid, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getLogoutData(uid: Int, token: String): Observable<BaseResponse<String>> {
        return RetrofitManager.service.getLogoutData(uid, token)
                .compose(SchedulerUtils.ioToMain())
    }
}