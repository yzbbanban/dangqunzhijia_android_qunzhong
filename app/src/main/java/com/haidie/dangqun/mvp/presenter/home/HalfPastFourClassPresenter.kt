package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.HalfPastFourClassContract
import com.haidie.dangqun.mvp.model.bean.HalfPastFourClassData
import com.haidie.dangqun.mvp.model.home.HalfPastFourClassModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/12/11 13:39
 * description
 */
class HalfPastFourClassPresenter : BasePresenter<HalfPastFourClassContract.View>(),HalfPastFourClassContract.Presenter{
    private val halfPastFourClassModel by lazy { HalfPastFourClassModel() }
    override fun getHalfPastFourClassData(uid: Int, token: String, page: Int, size: Int, type: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = halfPastFourClassModel.getHalfPastFourClassData(uid, token, page, size, type)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<HalfPastFourClassData>("获取数据列表失败") {
                    override fun onNext(t: HalfPastFourClassData) {
                        mRootView?.apply {
                            dismissLoading()
                            setHalfPastFourClassData(t)
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
}