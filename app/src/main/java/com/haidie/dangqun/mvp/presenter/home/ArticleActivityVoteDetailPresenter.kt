package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.ArticleActivityVoteDetailContract
import com.haidie.dangqun.mvp.model.bean.VoteDetailData
import com.haidie.dangqun.mvp.model.home.ArticleActivityVoteDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/12 14:21
 * description
 */
class ArticleActivityVoteDetailPresenter : BasePresenter<ArticleActivityVoteDetailContract.View>(),ArticleActivityVoteDetailContract.Presenter{

    private val articleActivityVoteDetailModel by lazy { ArticleActivityVoteDetailModel() }
    override fun getVoteDetailData(id: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = articleActivityVoteDetailModel.getVoteDetailData(id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<VoteDetailData>("获取详细信息失败"){
                    override fun onNext(t: VoteDetailData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setVoteDetailData(t)
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.apply {
                            dismissLoading()
                            showError(e.mMessage,e.errorCode)
                        }
                    }
                })

        addSubscription(disposable)
    }
    override fun getAddVoteInData(vid: Int, vInfoId: String, uid: Int) {
        val disposable = articleActivityVoteDetailModel.getAddVoteInData(vid, vInfoId, uid)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>(""){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setAddVoteInData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setAddVoteInData(false,e.mMessage)
                    }
                })

        addSubscription(disposable)
    }
}