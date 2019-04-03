package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.ActivityRecordDetailContract
import com.haidie.dangqun.mvp.model.bean.ActivityRecordDetailData
import com.haidie.dangqun.mvp.model.home.ActivityRecordDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/11/29 14:42
 * description
 */
class ActivityRecordDetailPresenter : BasePresenter<ActivityRecordDetailContract.View>(),ActivityRecordDetailContract.Presenter{
    private val activityRecordDetailModel by lazy { ActivityRecordDetailModel() }
    override fun getActivityRecordDetailData(uid: Int, token: String, id: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = activityRecordDetailModel.getActivityRecordDetailData(uid, token, id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<ActivityRecordDetailData>("获取活动记录详情失败") {
                    override fun onNext(t: ActivityRecordDetailData) {
                        mRootView?.apply {
                            dismissLoading()
                            setActivityRecordDetailData(t)
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