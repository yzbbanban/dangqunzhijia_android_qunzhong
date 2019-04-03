package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.WorkOrderManagementContract
import com.haidie.dangqun.mvp.event.WorkOrderManagementDetailEditStatus
import com.haidie.dangqun.mvp.model.bean.OrderListData
import com.haidie.dangqun.mvp.model.home.WorkOrderManagementModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/08 10:06
 * description
 */
class WorkOrderManagementPresenter : BasePresenter<WorkOrderManagementContract.View>(), WorkOrderManagementContract.Presenter{
    private val workOrderManagementModel by lazy { WorkOrderManagementModel() }

    override fun attachView(mRootView: WorkOrderManagementContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(WorkOrderManagementDetailEditStatus::class.java)
                .subscribe({ mRootView!!.showRefreshEvent()}))
    }
    override fun getOrderListData(uid: Int, page: Int, size: Int, id: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = workOrderManagementModel.getOrderListData(uid, page, size, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<OrderListData>("获取数据列表失败"){

                    override fun onNext(t: OrderListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setOrderListData(t)
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