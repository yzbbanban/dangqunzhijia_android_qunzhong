package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.ActivityRecordContract
import com.haidie.dangqun.mvp.model.bean.MyVolunteerActivitiesListData
import com.haidie.dangqun.mvp.model.home.ActivityRecordModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/11/29 14:13
 * description
 */
class ActivityRecordPresenter : BasePresenter<ActivityRecordContract.View>(),ActivityRecordContract.Presenter{
    private val activityRecordModel by lazy { ActivityRecordModel() }
    override fun getActivityRecordListData(uid: Int, token: String, page: Int, size: Int, type: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = activityRecordModel.getActivityRecordListData(uid, token, page, size, type)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<MyVolunteerActivitiesListData>("") {
                    override fun onNext(t: MyVolunteerActivitiesListData) {
                        mRootView?.apply {
                            dismissLoading()
                            setActivityRecordListData(t)
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.apply {
                            dismissLoading()
                            showError(e.mMessage, e.errorCode)
                        }
                    }
                })
        addSubscription(disposable)
    }
}