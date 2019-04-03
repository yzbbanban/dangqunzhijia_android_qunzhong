package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.MyHelpListContract
import com.haidie.dangqun.mvp.event.ReloadMyHelpEvent
import com.haidie.dangqun.mvp.model.bean.OnlineHelpListData
import com.haidie.dangqun.mvp.model.mine.MyHelpListModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/10/23 14:23
 * description
 */
class MyHelpListPresenter : BasePresenter<MyHelpListContract.View>(), MyHelpListContract.Presenter {

    private val myHelpListModel by lazy { MyHelpListModel() }
    override fun attachView(mRootView: MyHelpListContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReloadMyHelpEvent::class.java)
                .subscribe { mRootView!!.reloadMyHelp() })
    }
    override fun getOnlineHelpData(uid: Int, page: Int, size: Int, id: Int, check_status: Int, status: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = myHelpListModel.getOnlineHelpData(uid, page, size, id, check_status, status, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<OnlineHelpListData>("获取网上求助列表失败") {
                    override fun onNext(t: OnlineHelpListData) {
                        mRootView?.apply {
                            dismissLoading()
                            setMyHelpListData(t)
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
    override fun getOnlineHelpSubmitAgainData(uid: Int, token: String, id: Int, status: Int) {
        val disposable = myHelpListModel.getOnlineHelpSubmitAgainData(uid, token, id, status)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("再次提交失败"){
                    override fun onNext(t: Boolean) {
                        mRootView?.setSubmitAgainData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.setSubmitAgainData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
    override fun getOnlineHelpDeleteData(uid: Int, token: String, id: Int, status: Int) {
        val disposable = myHelpListModel.getOnlineHelpDeleteData(uid, token, id, status)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("删除失败"){
                    override fun onNext(t: Boolean) {
                        mRootView?.setDeleteData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.setDeleteData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}