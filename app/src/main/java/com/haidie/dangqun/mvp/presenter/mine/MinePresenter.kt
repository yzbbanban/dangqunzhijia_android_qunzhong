package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.Constants
import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.MineContract
import com.haidie.dangqun.mvp.event.MineEvent
import com.haidie.dangqun.mvp.model.bean.MineData
import com.haidie.dangqun.mvp.model.mine.MineModel
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils
import com.haidie.dangqun.utils.Preference

/**
 * Created by admin2
 *  on 2018/08/10  15:07
 * description
 */
class MinePresenter : BasePresenter<MineContract.View>(), MineContract.Presenter{
    private var loginStatus: Boolean by Preference(Constants.LOGIN_STATUS, false)
    private val mineModel  by lazy { MineModel() }

    override fun attachView(mRootView: MineContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(MineEvent::class.java)
                .subscribe { mRootView?.showRefreshEvent()})
    }

    override fun getMineData(uid: Int,token: String) {
        //检查是否绑定View
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = mineModel.getMineData( uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<MineData>("获取我的数据失败"){
                    override fun onNext(t: MineData) {
                        mRootView?.apply {
                            dismissLoading()
                            setMineData(t)
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

    override fun getLogoutData(uid: Int, token: String) {
        val disposable = mineModel.getLogoutData(uid, token)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<String>("退出失败"){
                    override fun onNext(t: String) {
                        loginStatus = false
                        mRootView?.logoutSuccess()
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.apply {
                            if (e.errorCode == ApiErrorCode.SUCCESS) {
                                loginStatus = false
                                logoutSuccess()
                            } else {
                                showError(e.mMessage, e.errorCode)
                            }
                        }
                    }
                })

        addSubscription(disposable)
    }
}