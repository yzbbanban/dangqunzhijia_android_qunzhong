package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.ExchangeDataListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/13 15:57
 * description
 */
class ExchangeDataListModel {
    fun getMyExchangeDataListData( uid: Int,  page: Int, size: Int, token: String): Observable<BaseResponse<ExchangeDataListData>>{
        return RetrofitManager.service.getMyExchangeDataListData(uid, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }
}