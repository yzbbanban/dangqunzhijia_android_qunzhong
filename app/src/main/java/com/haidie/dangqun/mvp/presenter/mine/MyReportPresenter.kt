package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.MyReportContract
import com.haidie.dangqun.mvp.event.ReloadMyReportEvent
import com.haidie.dangqun.rx.RxBus

/**
 * Create by   Administrator
 *      on     2018/10/23 14:23
 * description
 */
class MyReportPresenter : BasePresenter<MyReportContract.View>(), MyReportContract.Presenter {

    override fun attachView(mRootView: MyReportContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReloadMyReportEvent::class.java)
                .subscribe { mRootView!!.reloadMyReport() })
    }
}