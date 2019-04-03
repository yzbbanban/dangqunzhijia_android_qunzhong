package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.WorkOrderManagementDetailContract
import com.haidie.dangqun.mvp.event.WorkOrderManagementDetailEditStatus
import com.haidie.dangqun.mvp.model.bean.HistoryReplayData
import com.haidie.dangqun.mvp.model.home.WorkOrderManagementDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Create by   Administrator
 *      on     2018/09/08 11:26
 * description
 */
class WorkOrderManagementDetailPresenter : BasePresenter<WorkOrderManagementDetailContract.View>(),WorkOrderManagementDetailContract.Presenter{

    private val workOrderManagementDetailModel by lazy { WorkOrderManagementDetailModel() }
    override fun attachView(mRootView: WorkOrderManagementDetailContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(WorkOrderManagementDetailEditStatus::class.java)
                .subscribe{ mRootView!!.showRefreshEvent()})
    }
    override fun getOrderInfoData(uid: Int, id: Int, token: String, page: Int,size: Int) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = workOrderManagementDetailModel.getOrderInfoData(uid, id, token)
                .doOnNext{
                    mRootView!!.setOrderInfoData(it.data!!)
                }
                .observeOn(Schedulers.io())
                .flatMap{ workOrderManagementDetailModel.getHistoryReplayData(uid, id, page, size, token) }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<HistoryReplayData>("获取详情失败"){
                    override fun onNext(t: HistoryReplayData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setHistoryReplayData(t)
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

    override fun getToReplayData(uid: Int, id: Int, title: String, content: String, token: String) {
        val disposable = workOrderManagementDetailModel.getToReplayData(uid, id, title, content, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("回复失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setToReplayData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setToReplayData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }

    override fun getEditStatusData(uid: Int, id: Int, status: Int, token: String) {
        val disposable = workOrderManagementDetailModel.getEditStatusData(uid, id, status, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("操作失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setEditStatusData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setEditStatusData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}