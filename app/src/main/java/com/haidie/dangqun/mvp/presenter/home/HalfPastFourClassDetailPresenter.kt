package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.HalfPastFourClassDetailContract
import com.haidie.dangqun.mvp.model.bean.HalfPastFourClassDetailData
import com.haidie.dangqun.mvp.model.home.HalfPastFourClassDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/12/11 14:29
 * description
 */
class HalfPastFourClassDetailPresenter : BasePresenter<HalfPastFourClassDetailContract.View>(),HalfPastFourClassDetailContract.Presenter{
    private val halfPastFourClassDetailModel by lazy { HalfPastFourClassDetailModel() }
    override fun getHalfPastFourClassDetailData(uid: Int, token: String, id: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = halfPastFourClassDetailModel.getHalfPastFourClassDetailData(uid, token, id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<HalfPastFourClassDetailData>("获取详情失败") {
                    override fun onNext(t: HalfPastFourClassDetailData) {
                        mRootView?.apply {
                            dismissLoading()
                            setHalfPastFourClassDetailData(t)
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