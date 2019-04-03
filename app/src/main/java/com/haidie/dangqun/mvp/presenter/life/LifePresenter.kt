package com.haidie.dangqun.mvp.presenter.life

import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.life.LifeContract
import com.haidie.dangqun.mvp.event.ReloadLifeEvent
import com.haidie.dangqun.rx.RxBus

/**
 * Created by admin2
 *  on 2018/08/13  19:57
 * description
 */
class LifePresenter : BasePresenter<LifeContract.View>(), LifeContract.Presenter {
    override fun attachView(mRootView: LifeContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReloadLifeEvent::class.java)
                .subscribe { mRootView!!.reloadLife() })
    }
}