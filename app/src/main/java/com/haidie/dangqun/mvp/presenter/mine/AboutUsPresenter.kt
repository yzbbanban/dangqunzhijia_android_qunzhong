package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.AboutUsContract
import com.haidie.dangqun.mvp.model.bean.CheckVersionData
import com.haidie.dangqun.mvp.model.mine.AboutUsModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/18 08:53
 * description
 */
class AboutUsPresenter : BasePresenter<AboutUsContract.View>(),AboutUsContract.Presenter{
    private val aboutUsModel by lazy { AboutUsModel() }
    override fun getCheckVersionData() {
        checkViewAttached()
        val disposable = aboutUsModel.getCheckVersionData()
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<CheckVersionData>("检查更新失败"){
                    override fun onNext(t: CheckVersionData) { mRootView!!.setCheckVersionData(t) }
                    override fun onFail(e: ApiException) {  mRootView!!.showError(e.mMessage,e.errorCode) }
                })
        addSubscription(disposable)
    }

}