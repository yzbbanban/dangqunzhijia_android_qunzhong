package com.haidie.dangqun.mvp.contract.home

import com.haidie.dangqun.base.IBaseView
import com.haidie.dangqun.base.IPresenter
import com.haidie.dangqun.mvp.model.bean.VoteDetailData

/**
 * Create by   Administrator
 *      on     2018/09/12 14:21
 * description
 */
class ArticleActivityVoteDetailContract {
    interface View : IBaseView{
        fun setVoteDetailData(voteDetailData: VoteDetailData)
        fun showError(msg : String,errorCode : Int)
        fun setAddVoteInData(isSuccess : Boolean,msg : String)
    }

    interface Presenter : IPresenter<View>{
        fun getVoteDetailData( id: String)
        fun getAddVoteInData(vid: Int,  vInfoId: String, uid: Int)
    }
}