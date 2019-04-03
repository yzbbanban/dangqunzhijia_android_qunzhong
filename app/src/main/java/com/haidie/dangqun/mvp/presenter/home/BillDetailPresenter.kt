package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.BillDetailContract
import com.haidie.dangqun.mvp.event.PayEvent
import com.haidie.dangqun.mvp.model.bean.BillDetailData
import com.haidie.dangqun.mvp.model.bean.PayResultData
import com.haidie.dangqun.mvp.model.bean.PrepaidOrderData
import com.haidie.dangqun.mvp.model.home.BillDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/08/29 16:26
 * description
 */
class BillDetailPresenter : BasePresenter<BillDetailContract.View>(), BillDetailContract.Presenter {

    private val billDetailModel by lazy { BillDetailModel() }
    override fun attachView(mRootView: BillDetailContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(PayEvent::class.java)
                .subscribe { mRootView?.showPayEvent(it.isSuccess) })
    }
    override fun getBillDetailData(uid: Int, id: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = billDetailModel.getBillDetailData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<BillDetailData>("获取缴费详情失败"){
                    override fun onNext(t: BillDetailData) {
                        mRootView?.apply {
                            dismissLoading()
                            setBillDetailData(t)
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
    override fun getPrepaidOrderData(uid: Int, token: String, orderNo: String, price: String, body: String) {
        val disposable = billDetailModel.getPrepaidOrderData(uid, token, orderNo, price, body)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<PrepaidOrderData>("统一下单失败"){
                    override fun onNext(t: PrepaidOrderData) {
                        mRootView?.setPrepaidOrderData(t)
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.showPayError(e.mMessage,e.errorCode)
                    }
                })
        addSubscription(disposable)
    }
    override fun getPayResult(uid: Int, token: String, orderNo: String) {
        val disposable = billDetailModel.getPayResult(uid, token, orderNo)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<PayResultData>("支付失败"){
                    override fun onNext(t: PayResultData) {
                        mRootView?.setPayResult(t)
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.showPayError(e.mMessage,e.errorCode)
                    }
                })
        addSubscription(disposable)
    }
}