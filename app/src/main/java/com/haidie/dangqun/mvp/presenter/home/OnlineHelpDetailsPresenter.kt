package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.OnlineHelpDetailsContract
import com.haidie.dangqun.mvp.model.bean.OnlineHelpDetailsData
import com.haidie.dangqun.mvp.model.home.OnlineHelpDetailsModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/12 18:15
 * description
 */
class OnlineHelpDetailsPresenter : BasePresenter<OnlineHelpDetailsContract.View>(), OnlineHelpDetailsContract.Presenter {

    private val onlineHelpDetailsModel by lazy { OnlineHelpDetailsModel() }
    override fun getOnlineHelpDetailsData(uid: Int, id: Int, token: String, page: Int, size: Int) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = onlineHelpDetailsModel.getOnlineHelpDetailsData(uid, id, token)
//                .doOnNext{
//                    mRootView!!.setOnlineHelpDetailsData(it.data!!)
//                }
//                .flatMap{ onlineHelpDetailsModel.getOnlineHelpHistoryReplayData(uid, id, page, size, token) }
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<OnlineHelpDetailsData>("获取网上求助详情失败"){
                    override fun onNext(t: OnlineHelpDetailsData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setOnlineHelpDetailsData(t)
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
    override fun getOnlineHelpToReplayData(uid: Int, token: String, id: Int, type: Int, content: String) {
        val disposable = onlineHelpDetailsModel.getOnlineHelpToReplayData(uid, token, id, type, content)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("留言失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setOnlineHelpToReplayData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setOnlineHelpToReplayData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}