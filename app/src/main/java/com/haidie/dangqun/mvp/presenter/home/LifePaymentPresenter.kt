package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.LifePaymentContract
import com.haidie.dangqun.mvp.event.ReloadLifePaymentEvent
import com.haidie.dangqun.mvp.model.bean.BoundPaymentAccountListData
import com.haidie.dangqun.mvp.model.home.LifePaymentModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/11/27 14:21
 * description
 */
class LifePaymentPresenter : BasePresenter<LifePaymentContract.View>(),LifePaymentContract.Presenter{
    private val lifePaymentModel by lazy { LifePaymentModel() }
    override fun attachView(mRootView: LifePaymentContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReloadLifePaymentEvent::class.java)
                .subscribe { mRootView!!.showRefreshEvent() })
    }
    override fun getBoundPaymentAccountListData(uid: Int, token: String) {
        checkViewAttached()
        val disposable = lifePaymentModel.getBoundPaymentAccountListData(uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<BoundPaymentAccountListData>>("获取绑定的缴费账户列表名单失败") {
                    override fun onNext(t: ArrayList<BoundPaymentAccountListData>) {
                        mRootView?.setBoundPaymentAccountListData(t)
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.showError(e.mMessage, e.errorCode)
                    }
                })
        addSubscription(disposable)
    }
}