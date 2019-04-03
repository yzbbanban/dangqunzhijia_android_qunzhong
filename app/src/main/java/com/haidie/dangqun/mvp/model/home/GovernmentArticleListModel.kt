package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.GovernmentArticleListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/13 14:00
 * description
 */
class GovernmentArticleListModel {
    fun getGovernmentArticleListData(uid: Int,  page: Int, size: Int,cid: Int,  token: String,type: Int?): Observable<BaseResponse<GovernmentArticleListData>>{
        return RetrofitManager.service.getGovernmentArticleListData(uid, page, size, cid, token,type)
                .compose(SchedulerUtils.ioToMain())
    }
}