package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.VolunteerBindingContract
import com.haidie.dangqun.mvp.model.bean.VolunteerInfoData
import com.haidie.dangqun.mvp.model.mine.VolunteerBindingModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils

/**
 * Create by   Administrator
 *      on     2018/10/22 14:25
 * description
 */
class VolunteerBindingPresenter : BasePresenter<VolunteerBindingContract.View>(),VolunteerBindingContract.Presenter{
    private val volunteerBindingModel by lazy { VolunteerBindingModel() }
    override fun getVolunteerInfoData(uid: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable =  volunteerBindingModel.getVolunteerInfoData(uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<VolunteerInfoData>("获取志愿者信息失败"){
                    override fun onNext(t: VolunteerInfoData) {
                        mRootView?.apply {
                            dismissLoading()
                            setVolunteerInfoData(t)
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.apply {
                            dismissLoading()
                            showError(e.mMessage,e.errorCode)
                        }
                    }
                })
        addSubscription(disposable)
    }
}