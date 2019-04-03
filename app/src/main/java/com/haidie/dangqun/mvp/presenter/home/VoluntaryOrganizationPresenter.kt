package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.VoluntaryOrganizationContract
import com.haidie.dangqun.mvp.model.bean.VoluntaryOrganizationListData
import com.haidie.dangqun.mvp.model.home.VoluntaryOrganizationModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/11 15:29
 * description
 */
class VoluntaryOrganizationPresenter : BasePresenter<VoluntaryOrganizationContract.View>(),VoluntaryOrganizationContract.Presenter{
    private val voluntaryOrganizationModel by lazy { VoluntaryOrganizationModel() }
    override fun getVoluntaryOrganizationListData(uid: Int, page: Int, size: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = voluntaryOrganizationModel.getVoluntaryOrganizationListData(uid, page, size, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<VoluntaryOrganizationListData>("获取志愿组织列表失败"){
                    override fun onNext(t: VoluntaryOrganizationListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setVoluntaryOrganizationListData(t)
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