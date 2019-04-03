package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.VolunteerDetailContract
import com.haidie.dangqun.mvp.model.bean.VolunteerDetailData
import com.haidie.dangqun.mvp.model.home.VolunteerDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/12 09:21
 * description
 */
class VolunteerDetailPresenter : BasePresenter<VolunteerDetailContract.View>(),VolunteerDetailContract.Presenter{

    private val volunteerDetailModel by lazy { VolunteerDetailModel() }
    override fun getVolunteerDetailData(uid: Int, id: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = volunteerDetailModel.getVolunteerDetailData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<VolunteerDetailData>("获取志愿者详情失败"){
                    override fun onNext(t: VolunteerDetailData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setVolunteerDetailData(t)
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
    override fun getEditAdministratorStatusData(uid: Int, id: Int, status: Int, token: String) {
        val disposable = volunteerDetailModel.getEditAdministratorStatusData(uid, id, status, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("设置失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setEditAdministratorStatusData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setEditAdministratorStatusData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }

    override fun getEditPendingVolunteerChangeData(uid: Int, id: Int, status: Int, token: String) {
        val disposable = volunteerDetailModel.getEditPendingVolunteerChangeData(uid, id, status, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("审核失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setEditPendingVolunteerChangeData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setEditPendingVolunteerChangeData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
}