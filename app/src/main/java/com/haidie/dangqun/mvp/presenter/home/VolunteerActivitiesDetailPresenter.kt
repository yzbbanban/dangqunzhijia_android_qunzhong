package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.VolunteerActivitiesDetailContract
import com.haidie.dangqun.mvp.model.bean.VolunteerActivitiesDetailData
import com.haidie.dangqun.mvp.model.home.VolunteerActivitiesDetailModel
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/11 13:27
 * description
 */
class VolunteerActivitiesDetailPresenter : BasePresenter<VolunteerActivitiesDetailContract.View>(),VolunteerActivitiesDetailContract.Presenter{

    private val volunteerActivitiesDetailModel by lazy { VolunteerActivitiesDetailModel() }
    override fun getVolunteerActivitiesDetailData(uid: Int, id: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = volunteerActivitiesDetailModel.getVolunteerActivitiesDetailData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<List<VolunteerActivitiesDetailData>>("获取志愿者活动详情失败"){
                    override fun onNext(t: List<VolunteerActivitiesDetailData>) {
                        mRootView!!.apply {
                            dismissLoading()
                            setVolunteerActivitiesDetailData(t[0])
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
    override fun getAddActivityData(uid: Int, id: Int, token: String) {
        val disposable = volunteerActivitiesDetailModel.getAddActivityData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("报名失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setAddActivityData(t,"", ApiErrorCode.SUCCESS)
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setAddActivityData(false,e.mMessage,e.errorCode)
                    }
                })
        addSubscription(disposable)
    }
    override fun getSignInData(uid: Int, id: Int, token: String) {
        val disposable = volunteerActivitiesDetailModel.getSignInData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("签到失败"){
                    override fun onNext(t: Boolean) {
                        mRootView!!.setSignInData(t,"")
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.setSignInData(false,e.mMessage)
                    }
                })
        addSubscription(disposable)
    }
    override fun getVolunteerActivitiesSignInData(uid: Int, id: Int, token: String) {
        val disposable = volunteerActivitiesDetailModel.getVolunteerActivitiesSignInData(uid, id, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Boolean>("签到失败") {
                    override fun onNext(t: Boolean) {
                        mRootView?.setVolunteerActivitiesSignInData(t, "",ApiErrorCode.SUCCESS)
                    }

                    override fun onFail(e: ApiException) {
                        mRootView?.setVolunteerActivitiesSignInData(false, e.mMessage,e.errorCode)
                    }
                })
        addSubscription(disposable)
    }
}