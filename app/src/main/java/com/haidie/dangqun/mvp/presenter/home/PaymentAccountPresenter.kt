package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.PaymentAccountContract
import com.haidie.dangqun.mvp.model.bean.BoundPaymentAccountListData
import com.haidie.dangqun.mvp.model.home.PaymentAccountModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/11/27 16:23
 * description
 */
class PaymentAccountPresenter : BasePresenter<PaymentAccountContract.View>(),PaymentAccountContract.Presenter{

    private val paymentAccountModel by lazy { PaymentAccountModel() }
    override fun getBoundPaymentAccountListData(uid: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = paymentAccountModel.getBoundPaymentAccountListData(uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ArrayList<BoundPaymentAccountListData>>("获取绑定的缴费账户列表名单失败") {
                    override fun onNext(t: ArrayList<BoundPaymentAccountListData>) {
                        mRootView?.apply {
                            dismissLoading()
                            setBoundPaymentAccountListData(t)
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.apply {
                            dismissLoading()
                            showError(e.mMessage, e.errorCode)
                        }
                    }
                })
        addSubscription(disposable)
    }

    override fun getUnbindPaymentAccountResultData(uid: Int, token: String, id: Int) {
        val disposable = paymentAccountModel.getUnbindPaymentAccountResultData(uid, token, id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("解绑失败") {
                    override fun onNext(t: Boolean) {
                        mRootView?.setUnbindPaymentAccountResultData(t, "")
                    }

                    override fun onFail(e: ApiException) {
                        mRootView?.setUnbindPaymentAccountResultData(false, e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}