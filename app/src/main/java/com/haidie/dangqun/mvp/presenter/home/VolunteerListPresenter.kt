package com.haidie.dangqun.mvp.presenter.home

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.home.VolunteerListContract
import com.haidie.dangqun.mvp.event.EditAdministratorStatusEvent
import com.haidie.dangqun.mvp.model.bean.VolunteerListData
import com.haidie.dangqun.mvp.model.home.VolunteerListModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/09/11 16:52
 * description
 */
class VolunteerListPresenter : BasePresenter<VolunteerListContract.View>(),VolunteerListContract.Presenter{
    private val volunteerListModel by lazy { VolunteerListModel() }

    override fun attachView(mRootView: VolunteerListContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(EditAdministratorStatusEvent::class.java)
                .subscribe{
                    mRootView!!.showRefreshEvent()
                })
    }
    override fun getVolunteerListData(uid: Int, group_id: Int, type: Int, search: String, page: Int, size: Int, token: String) {
        checkViewAttached()
        mRootView!!.showLoading()
        val disposable = volunteerListModel.getVolunteerListData(uid, group_id, type, search, page, size, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<VolunteerListData>("获取志愿者列表失败"){
                    override fun onNext(t: VolunteerListData) {
                        mRootView!!.apply {
                            dismissLoading()
                            setVolunteerListData(t)
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