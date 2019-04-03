package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.PointsMallListContract
import com.haidie.dangqun.mvp.model.bean.PointsMallListData
import com.haidie.dangqun.mvp.model.home.PointsMallListModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/11 09:27
 * description
 */
class PointsMallListPresenter : BasePresenter<PointsMallListContract.View>(),PointsMallListContract.Presenter{

    private val pointsMallListModel by lazy { PointsMallListModel() }
    override fun getPointsMallListData(uid: Int, type: Int, page: Int, size: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = pointsMallListModel.getPointsMallListData(uid, type, page, size, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<PointsMallListData>("获取积分商城数据失败"){
                    override fun onNext(t: PointsMallListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setPointsMallListData(t)
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

    override fun getExchangeData(uid: Int, id: Int, token: String) {
        val disposable = pointsMallListModel.getExchangeData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("兑换失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setExchangeData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setExchangeData(false,e.mMessage)
                    }
                })

        addSubscription(disposable)
    }
}