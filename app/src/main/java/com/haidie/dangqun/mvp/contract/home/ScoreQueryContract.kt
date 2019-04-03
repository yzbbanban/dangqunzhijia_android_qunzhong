package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.ScoreQueryData

/**
 * Create by   Administrator
 *      on     2018/11/29 09:25
 * description
 */
class ScoreQueryContract {
    interface View : IBaseView{
        fun setScoreQueryData(scoreQueryData: ScoreQueryData)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getScoreQueryData( uid: Int, token: String, username: String,  year: String,
                               clazz: String,  type: Int)
    }
}