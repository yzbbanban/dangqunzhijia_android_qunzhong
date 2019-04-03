package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.ScoreQueryContract
import com.haidie.dangqun.mvp.model.bean.ScoreQueryData
import com.haidie.dangqun.mvp.model.home.ScoreQueryModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/11/29 09:25
 * description
 */
class ScoreQueryPresenter : BasePresenter<ScoreQueryContract.View>(),ScoreQueryContract.Presenter{
    private val scoreQueryModel by lazy { ScoreQueryModel() }
    override fun getScoreQueryData(uid: Int, token: String, username: String, year: String, clazz: String, type: Int) {
        checkViewAttached()
        val disposable = scoreQueryModel.getScoreQueryData(uid, token, username, year, clazz, type)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ScoreQueryData>("查询失败") {
                    override fun onNext(t: ScoreQueryData) {
                        mRootView?.setScoreQueryData(t)
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.showError(e.mMessage, e.errorCode)
                    }
                })
        addSubscription(disposable)
    }
}