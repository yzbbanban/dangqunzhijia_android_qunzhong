package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.ActivityDetailData
import com.haidie.dangqun.mvp.model.bean.ArticleDetailData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/10 09:56
 * description
 */
class LifeBulletinDetailModel {
    fun getArticleDetailData(id: String): Observable<BaseResponse<List<ArticleDetailData>>>{
        return RetrofitManager.service.getArticleDetailData(id)
                .compose(SchedulerUtils.ioToMain())
    }
    fun getActivityDetailData( id: String, uid: Int,  token: String) : Observable<BaseResponse<List<ActivityDetailData>>>{
        return RetrofitManager.service.getActivityDetailData(id, uid, token)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getAddActivityData( uid: Int, id: Int, token: String): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getAddActivityData(uid, id, token)
                .compose(SchedulerUtils.ioToMain())
    }
}