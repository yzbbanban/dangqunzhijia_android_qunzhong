package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.CommodityClassificationListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/10/12 13:26
 * description
 */
class CommodityTradingModel {
    fun getCommodityClassificationListData(uid: Int, token: String) : Observable<BaseResponse<ArrayList<CommodityClassificationListData>>> {
        return RetrofitManager.service.getCommodityClassificationListData(uid, token)
                .compose(SchedulerUtils.ioToMain())
    }
}