package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.FreshAirActivitiesContract
import com.haidie.dangqun.mvp.model.bean.FreshAirActivitiesData
import com.haidie.dangqun.mvp.model.home.FreshAirActivitiesModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/12/10 13:59
 * description
 */
class FreshAirActivitiesPresenter : BasePresenter<FreshAirActivitiesContract.View>(),FreshAirActivitiesContract.Presenter{
    private val freshAirActivitiesModel by lazy { FreshAirActivitiesModel() }
    override fun getFreshAirActivitiesData(user_id: Int, token: String, page: Int, size: Int, module_type: Int, is_register_copy: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = freshAirActivitiesModel.getFreshAirActivitiesData(user_id, token, page, size, module_type, is_register_copy)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<FreshAirActivitiesData>("获取新风活动列表失败") {

                    override fun onNext(t: FreshAirActivitiesData) {
                        mRootView?.apply {
                            dismissLoading()
                            setFreshAirActivitiesData(t)
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