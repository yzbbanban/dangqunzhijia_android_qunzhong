package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.VolunteerActivitiesListContract
import com.haidie.dangqun.mvp.model.bean.MyVolunteerActivitiesListData
import com.haidie.dangqun.mvp.model.home.VolunteerActivitiesListModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/11 11:27
 * description
 */
class VolunteerActivitiesListPresenter : BasePresenter<VolunteerActivitiesListContract.View>(),VolunteerActivitiesListContract.Presenter{

    private val volunteerActivitiesListModel by lazy { VolunteerActivitiesListModel() }
    override fun getVolunteerActivitiesListData(uid: Int, groupid: Int?, page: Int, size: Int, type: Int, status: Int,token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = volunteerActivitiesListModel.getVolunteerActivitiesListData(uid, groupid, page, size, type, status, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<MyVolunteerActivitiesListData>("获取志愿者活动列表失败"){
                    override fun onNext(t: MyVolunteerActivitiesListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setMyVolunteerActivitiesListData(t)
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
    override fun getMyVolunteerActivitiesListData(uid: Int, token: String, page: Int, size: Int) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = volunteerActivitiesListModel.getMyVolunteerActivitiesListData(uid, token, page, size)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<MyVolunteerActivitiesListData>("获取我的志愿者活动列表失败"){
                    override fun onNext(t: MyVolunteerActivitiesListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setMyVolunteerActivitiesListData(t)
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