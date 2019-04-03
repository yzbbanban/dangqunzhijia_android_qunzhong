package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.IReleasedContract
import com.haidie.dangqun.mvp.event.ReleaseActivitiesEvent
import com.haidie.dangqun.mvp.model.bean.IReleasedData
import com.haidie.dangqun.mvp.model.mine.IReleasedModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/14 15:26
 * description
 */
class IReleasedPresenter : BasePresenter<IReleasedContract.View>(),IReleasedContract.Presenter{
    private val iReleasedModel by lazy { IReleasedModel() }
    override fun attachView(mRootView: IReleasedContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(ReleaseActivitiesEvent::class.java)
                .subscribe { mRootView!!.showRefreshEvent() })
    }

    override fun getIReleasedData(uid: Int, page: Int, size: Int,  token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = iReleasedModel.getIReleasedData(uid, page, size,  token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<IReleasedData>("获取我发布的活动列表失败"){
                    override fun onNext(t: IReleasedData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setIReleasedData(t)
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