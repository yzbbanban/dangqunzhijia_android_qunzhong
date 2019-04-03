package com.haidie.dangqun.mvp.model.business

import com.haidie.dangqun.mvp.model.bean.CommodityClassificationListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by admin2
 *  on 2018/08/13  19:59
 * description
 */
class BusinessModel {
    fun getCommodityClassificationListData(uid: Int, token: String) : Observable<BaseResponse<ArrayList<CommodityClassificationListData>>> {
        return RetrofitManager.service.getCommodityClassificationListData(uid, token)
                .compose(SchedulerUtils.ioToMain())
    }
}