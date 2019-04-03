package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.FreshAirActivitiesDetailContract
import com.haidie.dangqun.mvp.model.bean.FreshAirActivitiesDetailData
import com.haidie.dangqun.mvp.model.home.FreshAirActivitiesDetailModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/12/10 14:57
 * description
 */
class FreshAirActivitiesDetailPresenter : BasePresenter<FreshAirActivitiesDetailContract.View>(),FreshAirActivitiesDetailContract.Presenter{
    private val freshAirActivitiesDetailModel by lazy { FreshAirActivitiesDetailModel() }
    override fun getFreshAirActivitiesDetailData(user_id: Int, token: String, article_id: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = freshAirActivitiesDetailModel.getFreshAirActivitiesDetailData(user_id, token, article_id)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<FreshAirActivitiesDetailData>("获取新风活动详情失败") {
                    override fun onNext(t: FreshAirActivitiesDetailData) {
                        mRootView?.apply {
                            dismissLoading()
                            setFreshAirActivitiesDetailData(t)
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