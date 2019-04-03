package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.VolunteersParticipateActivitiesContract
import com.haidie.dangqun.mvp.model.bean.VolunteersParticipateActivitiesListData
import com.haidie.dangqun.mvp.model.home.VolunteersParticipateActivitiesModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/11 14:17
 * description
 */
class VolunteersParticipateActivitiesPresenter : BasePresenter<VolunteersParticipateActivitiesContract.View>(),VolunteersParticipateActivitiesContract.Presenter{
    private val volunteersParticipateActivitiesModel by lazy { VolunteersParticipateActivitiesModel() }
    override fun getVolunteersParticipateActivitiesListData(uid: Int, id: Int, page: Int, size: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = volunteersParticipateActivitiesModel.getVolunteersParticipateActivitiesListData(uid, id, page, size, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<VolunteersParticipateActivitiesListData>("获取参加活动人员列表失败"){
                    override fun onNext(t: VolunteersParticipateActivitiesListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setVolunteersParticipateActivitiesListData(t)
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