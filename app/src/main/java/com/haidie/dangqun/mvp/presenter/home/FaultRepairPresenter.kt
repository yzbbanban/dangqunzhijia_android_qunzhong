package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.FaultRepairContract
import com.haidie.dangqun.mvp.event.ReloadFaultRepairEvent
import com.haidie.dangqun.rx.RxBus

/**
 * Create by   Administrator
 *      on     2018/09/22 14:00
 * description
 */
class FaultRepairPresenter : BasePresenter<FaultRepairContract.View>(),FaultRepairContract.Presenter{
    override fun attachView(mRootView: FaultRepairContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReloadFaultRepairEvent::class.java)
                .subscribe { mRootView!!.showRefreshEvent() })
    }
}