package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.EvaluationListContract
import com.haidie.dangqun.mvp.model.bean.EvaluationListData
import com.haidie.dangqun.mvp.model.home.EvaluationListModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/12 11:59
 * description
 */
class EvaluationListPresenter : BasePresenter<EvaluationListContract.View>(),EvaluationListContract.Presenter{
    private val evaluationListModel by lazy { EvaluationListModel() }
    override fun getEvaluationListData(uid: Int, category: Int, page: Int, size: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = evaluationListModel.getEvaluationListData(uid, category, page, size, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<EvaluationListData>("获取评比评选列表失败"){
                    override fun onNext(t: EvaluationListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setEvaluationListData(t)
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