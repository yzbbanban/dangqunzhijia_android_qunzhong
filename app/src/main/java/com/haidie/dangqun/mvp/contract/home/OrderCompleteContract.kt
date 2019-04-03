package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/09/10 14:22
 * description
 */
class OrderCompleteContract {
    interface View : IBaseView{
        fun setSubmitEvaluationData(isSuccess : Boolean,msg : String)
    }

    interface Presenter : IPresenter<View>{
        fun getSubmitEvaluationData( uid: Int,  id: Int, rank: Int,  content: String,token: String)
    }
}