package com.haidie.dangqun.mvp.presenter.main

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.main.MainContract
import com.haidie.dangqun.mvp.event.RegisterXGEvent
import com.haidie.dangqun.mvp.event.SwitchToBusinessEvent
import com.haidie.dangqun.net.RetrofitManager
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils
import com.haidie.dangqun.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/09/13 13:25
 * description
 */
class MainPresenter : BasePresenter<MainContract.View>(),MainContract.Presenter{

    override fun attachView(mRootView: MainContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(SwitchToBusinessEvent::class.java)
                .subscribe {mRootView!!.switchToBusiness() })
        addSubscription(
                RxBus.getDefault().toFlowable(RegisterXGEvent::class.java)
                        .subscribe { mRootView?.initXG() })
    }
    override fun getXGData(admin_id: Int, device_token: String,token: String) {
        val disposable = RetrofitManager.service.getXGData(admin_id, device_token,token)
                .compose(SchedulerUtils.ioToMain())
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<String>("") {
                    override fun onFail(e: ApiException) {}
                    override fun onNext(t: String) {}
                })
        addSubscription(disposable)
    }
}