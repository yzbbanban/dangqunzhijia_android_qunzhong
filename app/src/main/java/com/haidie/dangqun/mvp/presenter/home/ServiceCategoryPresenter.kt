package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.ServiceCategoryContract
import com.haidie.dangqun.mvp.model.bean.ServiceCategoryData
import com.haidie.dangqun.mvp.model.home.ServiceCategoryModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/10 10:47
 * description
 */
class ServiceCategoryPresenter : BasePresenter<ServiceCategoryContract.View>(), ServiceCategoryContract.Presenter {

    private val serviceCategoryModel by lazy { ServiceCategoryModel() }
    override fun getServiceCategoryData(id: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = serviceCategoryModel.getServiceCategoryData(id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ServiceCategoryData>("获取服务分类列表失败"){

                    override fun onNext(t: ServiceCategoryData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setServiceCategoryData(t)
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