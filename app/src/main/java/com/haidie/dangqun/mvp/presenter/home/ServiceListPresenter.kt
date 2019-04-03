package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.ServiceListContract
import com.haidie.dangqun.mvp.model.bean.ServiceListData
import com.haidie.dangqun.mvp.model.home.ServiceListModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/10 11:29
 * description
 */
class ServiceListPresenter : BasePresenter<ServiceListContract.View>(),ServiceListContract.Presenter{
    private val serviceListModel by lazy { ServiceListModel() }
    override fun getServiceListData(id: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = serviceListModel.getServiceListData(id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ServiceListData>("获取服务列表失败"){
                    override fun onNext(t: ServiceListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setServiceListData(t)
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