package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.OrderCompleteContract
import com.haidie.dangqun.mvp.model.home.OrderCompleteModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/10 14:23
 * description
 */
class OrderCompletePresenter : BasePresenter<OrderCompleteContract.View>(),OrderCompleteContract.Presenter{
    private val orderCompleteModel by lazy { OrderCompleteModel() }
    override fun getSubmitEvaluationData(uid: Int, id: Int, rank: Int, content: String, token: String) {
        checkViewAttached()
        val disposable = orderCompleteModel.getSubmitEvaluationData(uid, id, rank, content, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("评价失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setSubmitEvaluationData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setSubmitEvaluationData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}