package com.haidie.dangqun.mvp.model.mine

import com.haidie.dangqun.mvp.model.bean.MyVoteListData
import com.haidie.dangqun.net.BaseResponse
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Create by   Administrator
 *      on     2018/09/17 13:35
 * description
 */
class MyVoteModel {
    fun getMyVoteListData( uid: Int, page: Int,  size: Int, token: String): Observable<BaseResponse<MyVoteListData>>{
        return RetrofitManager.service.getMyVoteListData(uid, page, size, token)
                .compose(SchedulerUtils.ioToMain())
    }
}