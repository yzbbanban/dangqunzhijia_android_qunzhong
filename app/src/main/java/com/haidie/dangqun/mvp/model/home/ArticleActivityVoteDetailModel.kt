package com.haidie.dangqun.mvp.model.home

import com.haidie.dangqun.mvp.model.bean.VoteDetailData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/12 14:22
 * description
 */
class ArticleActivityVoteDetailModel {
    fun getVoteDetailData( id: String): Observable<BaseResponse<VoteDetailData>>{
        return RetrofitManager.service.getVoteDetailData(id)
                .compose(SchedulerUtils.ioToMain())
    }

    fun getAddVoteInData(vid: Int,  vInfoId: String, uid: Int): Observable<BaseResponse<Boolean>>{
        return RetrofitManager.service.getAddVoteInData(vid, vInfoId, uid)
                .compose(SchedulerUtils.ioToMain())
    }
}