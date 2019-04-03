package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/09/08 15:47
 * description
 */
class WorkOrderEvaluationContract {
    interface View : IBaseView{
        fun setToCommentData(isSuccess: Boolean,msg : String)
    }

    interface Presenter : IPresenter<View>{
        fun getToCommentData(uid: Int, id: Int, rank: Int, content: String, token: String, status: Int)
    }
}