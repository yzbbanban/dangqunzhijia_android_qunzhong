package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.VoluntaryOrganizationDetailContract
import com.haidie.dangqun.mvp.event.EditPendingVolunteerChangeEvent
import com.haidie.dangqun.mvp.event.VolunteerApplicationEvent
import com.haidie.dangqun.mvp.model.bean.VoluntaryOrganizationDetailData
import com.haidie.dangqun.mvp.model.home.VoluntaryOrganizationDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/11 16:01
 * description
 */
class VoluntaryOrganizationDetailPresenter : BasePresenter<VoluntaryOrganizationDetailContract.View>(),VoluntaryOrganizationDetailContract.Presenter{
    private val voluntaryOrganizationDetailModel by lazy { VoluntaryOrganizationDetailModel() }
    override fun attachView(mRootView: VoluntaryOrganizationDetailContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(VolunteerApplicationEvent::class.java)
                .subscribe{
                    mRootView!!.showRefreshEvent()
                })
        addSubscription(RxBus.getDefault().toFlowable(EditPendingVolunteerChangeEvent::class.java)
                .subscribe{
                    mRootView!!.showRefreshEvent()
                })

    }
    override fun getVoluntaryOrganizationDetailData(uid: Int, id: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = voluntaryOrganizationDetailModel.getVoluntaryOrganizationDetailData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<VoluntaryOrganizationDetailData>("获取志愿者组织详情失败"){

                    override fun onNext(t: VoluntaryOrganizationDetailData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setVoluntaryOrganizationDetailData(t)
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