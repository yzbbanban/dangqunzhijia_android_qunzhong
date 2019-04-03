package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.ServiceDetailContract
import com.haidie.dangqun.mvp.model.bean.ServiceDetailItemData
import com.haidie.dangqun.mvp.model.home.ServiceDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/10 13:12
 * description
 */
class ServiceDetailPresenter : BasePresenter<ServiceDetailContract.View>(),ServiceDetailContract.Presenter{
    private val serviceDetailModel by lazy { ServiceDetailModel() }
    override fun getServiceDetailData(id: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = serviceDetailModel.getServiceDetailData(id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<List<ServiceDetailItemData>>("获取服务详情失败"){
                    override fun onNext(t: List<ServiceDetailItemData>) {
                        mRootView!!.apply {
                            dismissLoading()
                            setServiceDetailData(t)
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