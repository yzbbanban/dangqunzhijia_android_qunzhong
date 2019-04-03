package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.SubmitWorkOrderContract
import com.haidie.dangqun.mvp.model.bean.ServiceCategoryData
import com.haidie.dangqun.mvp.model.home.SubmitWorkOrderModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/09/08 08:39
 * description
 */
class SubmitWorkOrderPresenter : BasePresenter<SubmitWorkOrderContract.View>(), SubmitWorkOrderContract.Presenter{

    private val submitWorkOrderModel by lazy { SubmitWorkOrderModel() }

    override fun getServiceCategoryData(id: String) {
        checkViewAttached()
        val disposable = submitWorkOrderModel.getServiceCategoryData(id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ServiceCategoryData>("获取工单类型失败"){

                    override fun onNext(t: ServiceCategoryData) {
                        mRootView!!.setServiceCategoryData(t)
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.showError(e.mMessage,e.errorCode)
                    }
                })

        addSubscription(disposable)
    }

    override fun getToWorkOrderData(uid: RequestBody, cid: RequestBody, title: RequestBody, content: RequestBody, token: RequestBody, parts: List<MultipartBody.Part>) {
        val disposable = submitWorkOrderModel.getToWorkOrderData(uid, cid, title, content, token, parts)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("提交失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setToWorkOrderData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setToWorkOrderData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}