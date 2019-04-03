package com.haidie.dangqun.mvp.presenter.life

import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.life.LifeListContract
import com.haidie.dangqun.mvp.event.ReloadLifeEvent
import com.haidie.dangqun.rx.RxBus

/**
 * Create by   Administrator
 *      on     2018/09/14 11:19
 * description
 */
class LifeListPresenter : BasePresenter<LifeListContract.View>(),LifeListContract.Presenter{
    override fun attachView(mRootView: LifeListContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReloadLifeEvent::class.java)
                .subscribe { mRootView!!.reloadLife() })
    }
}