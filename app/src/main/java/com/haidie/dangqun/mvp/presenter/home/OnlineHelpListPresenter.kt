package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.OnlineHelpListContract
import com.haidie.dangqun.mvp.model.bean.OnlineHelpListData
import com.haidie.dangqun.mvp.model.home.OnlineHelpListModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/12 15:47
 * description
 */
class OnlineHelpListPresenter : BasePresenter<OnlineHelpListContract.View>(),OnlineHelpListContract.Presenter{
    private val onlineHelpListModel by lazy { OnlineHelpListModel() }
    override fun getOnlineHelpListData(uid: Int, page: Int, size: Int, id: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = onlineHelpListModel.getOnlineHelpListData(uid, page, size, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<OnlineHelpListData>("获取网上求助列表失败"){
                    override fun onNext(t: OnlineHelpListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setOnlineHelpListData(t)
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