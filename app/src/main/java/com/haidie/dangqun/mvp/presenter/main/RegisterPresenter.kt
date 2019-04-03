package com.haidie.dangqun.mvp.presenter.main

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.main.RegisterContract
import com.haidie.dangqun.mvp.model.bean.RegisterData
import com.haidie.dangqun.mvp.model.main.RegisterModel
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Create by   Administrator
 *      on     2018/09/07 14:42
 * description
 */
class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter {
    private val registerModel by lazy { RegisterModel() }

    override fun getSendSMSData(mobile: String, event: String) {
        checkViewAttached()
        val disposable = registerModel.getSendSMSData(mobile, event)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<String>("发送验证码失败"){
                    override fun onNext(t: String) { mRootView!!.setSendSMSData() }
                    override fun onFail(e: ApiException) {  mRootView!!.showError(e.mMessage,e.errorCode) }
                })
        addSubscription(disposable)
    }
    override fun getRegisterData(event: String,mobile: String, nickname: String?, group_id: Int?, password: String, captcha: String) {
        checkViewAttached()
        val disposable = registerModel.getCheckVerificationCodeData(mobile, event, captcha)
                .doOnNext {
                    mRootView!!.setVerificationCodeData(it.message,it.code)
                }
                .flatMap{
                    if (it.code == ApiErrorCode.SUCCESS) {
                        registerModel.getRegisterData(mobile, nickname, group_id, password,captcha)
                    } else {
                        Observable.error(ApiException(it.code,it.message))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<RegisterData>("注册失败") {
                    override fun onNext(data: RegisterData) {  mRootView!!.setRegisterData(data) }
                    override fun onFail(e: ApiException) {  mRootView!!.showError(e.mMessage, e.errorCode)  }
                })
        addSubscription(disposable)
    }
}