package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.EvaluationListData

/**
 * Create by   Administrator
 *      on     2018/09/12 11:59
 * description
 */
class EvaluationListContract {
    interface View : IBaseView{
        fun setEvaluationListData(evaluationListData: EvaluationListData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View>{
        fun getEvaluationListData(uid: Int, category: Int, page: Int, size: Int,  token: String)
    }
}