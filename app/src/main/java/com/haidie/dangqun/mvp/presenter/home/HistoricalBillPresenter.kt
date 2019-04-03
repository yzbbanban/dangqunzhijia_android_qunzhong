package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.HistoricalBillContract
import com.haidie.dangqun.mvp.event.PayResultEvent
import com.haidie.dangqun.mvp.model.bean.HistoricalBillData
import com.haidie.dangqun.mvp.model.home.HistoricalBillModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/08/30 16:09
 * description
 */
class HistoricalBillPresenter : BasePresenter<HistoricalBillContract.View>(), HistoricalBillContract.Presenter {
    private val historicalBillModel by lazy { HistoricalBillModel() }
    override fun attachView(mRootView: HistoricalBillContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(PayResultEvent::class.java)
                .subscribe({ mRootView!!.showRefreshEvent() }))
    }
    override fun getHistoricalBillData(uid: Int, year: Int?, month: String?, page: Int, size: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = historicalBillModel.getHistoricalBillData(uid, year, month, page, size, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<HistoricalBillData>("获取历史账单列表失败"){

                    override fun onNext(t: HistoricalBillData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setHistoricalBillData(t.list)
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