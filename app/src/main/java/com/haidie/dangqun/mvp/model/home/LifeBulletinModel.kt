package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.ArticleListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/08 17:27
 * description
 */
class LifeBulletinModel {
    fun getArticleListData( id: String): Observable<BaseResponse<ArticleListData>>{
        return RetrofitManager.service.getArticleListData(id)
                .compose(SchedulerUtils.ioToMain())
    }
}