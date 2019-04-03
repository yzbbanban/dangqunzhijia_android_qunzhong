package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.OnlineOrderContract
import com.haidie.dangqun.mvp.model.home.OnlineOrderModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/10 13:45
 * description
 */
class OnlineOrderPresenter : BasePresenter<OnlineOrderContract.View>(),OnlineOrderContract.Presenter{
    private val onlineOrderModel by lazy { OnlineOrderModel() }
    override fun getCreateOrderData(uid: Int, sid: Int, username: String, phone: String, content: String, time: String, address: String, token: String) {
        checkViewAttached()
        val disposable = onlineOrderModel.getCreateOrderData(uid, sid, username, phone, content, time, address, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("提交失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setCreateOrderData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setCreateOrderData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}