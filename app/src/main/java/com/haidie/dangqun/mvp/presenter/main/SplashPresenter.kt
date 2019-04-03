package com.haidie.dangqun.mvp.presenter.main

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.main.SplashContract
import com.haidie.dangqun.mvp.model.bean.CheckVersionData
import com.haidie.dangqun.mvp.model.main.SplashModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils


/**
 * Create by   Administrator
 *      on     2018/08/29 10:11
 * description
 */
class SplashPresenter : BasePresenter<SplashContract.View>(), SplashContract.Presenter{
    private val splashModel by lazy { SplashModel() }
    override fun getCheckVersionData() {

        val disposable = splashModel.getCheckVersionData()
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<CheckVersionData>("获取数据失败") {
                    override fun onNext(data: CheckVersionData) {  mRootView!!.setCheckVersionData(data)  }
                    override fun onFail(e: ApiException) {  mRootView!!. showError(e.mMessage,e.errorCode) }
                })
        addSubscription(disposable)
    }
}