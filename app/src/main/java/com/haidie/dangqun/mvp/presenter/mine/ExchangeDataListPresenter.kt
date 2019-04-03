package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.ExchangeDataListContract
import com.haidie.dangqun.mvp.model.bean.ExchangeDataListData
import com.haidie.dangqun.mvp.model.mine.ExchangeDataListModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/13 15:56
 * description
 */
class ExchangeDataListPresenter : BasePresenter<ExchangeDataListContract.View>(),ExchangeDataListContract.Presenter{
    private val exchangeDataListModel by lazy { ExchangeDataListModel() }
    override fun getMyExchangeDataListData(uid: Int, page: Int, size: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = exchangeDataListModel.getMyExchangeDataListData(uid, page, size, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ExchangeDataListData>("获取我的兑换列表数据失败"){
                    override fun onNext(t: ExchangeDataListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setMyExchangeDataListData(t)
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