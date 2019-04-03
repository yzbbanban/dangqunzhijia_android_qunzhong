package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.LivingPaymentContract
import com.haidie.dangqun.mvp.event.PayResultEvent
import com.haidie.dangqun.mvp.model.bean.LivingPaymentData
import com.haidie.dangqun.mvp.model.home.LivingPaymentModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/08/29 11:20
 * description
 */
class LivingPaymentPresenter : BasePresenter<LivingPaymentContract.View>(), LivingPaymentContract.Presenter{
    private val livingPaymentModel by lazy { LivingPaymentModel() }
    override fun attachView(mRootView: LivingPaymentContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(PayResultEvent::class.java)
                .subscribe { mRootView?.showRefreshEvent() })
    }
    override fun getLivingPaymentData(uid: Int, token: String, page: Int, size: Int, id: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = livingPaymentModel.getLivingPaymentData(uid, token, page, size, id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<LivingPaymentData>("获取应收账单列表失败"){

                    override fun onNext(t: LivingPaymentData) {
                        mRootView?.apply {
                            dismissLoading()
                            setLivingPaymentData(t.list)
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.apply {
                            dismissLoading()
                            showError(e.mMessage,e.errorCode)
                        }
                    }
                })

        addSubscription(disposable)
    }
}