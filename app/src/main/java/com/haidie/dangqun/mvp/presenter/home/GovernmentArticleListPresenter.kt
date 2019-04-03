package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.GovernmentArticleListContract
import com.haidie.dangqun.mvp.model.bean.GovernmentArticleListData
import com.haidie.dangqun.mvp.model.home.GovernmentArticleListModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/13 13:59
 * description
 */
class GovernmentArticleListPresenter : BasePresenter<GovernmentArticleListContract.View>(),GovernmentArticleListContract.Presenter{
    private val governmentArticleListModel by lazy { GovernmentArticleListModel() }
    override fun getGovernmentArticleListData(uid: Int, page: Int, size: Int, cid: Int, token: String,type: Int?) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = governmentArticleListModel.getGovernmentArticleListData(uid, page, size, cid, token,type)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<GovernmentArticleListData>("获取文章列表失败"){
                    override fun onNext(t: GovernmentArticleListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setGovernmentArticleListData(t)
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
}