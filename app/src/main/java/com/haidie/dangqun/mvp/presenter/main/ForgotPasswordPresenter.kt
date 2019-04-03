package com.haidie.dangqun.mvp.presenter.main

import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.main.ForgotPasswordContract
import com.haidie.dangqun.mvp.model.main.ForgotPasswordModel
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Create by   Administrator
 *      on     2018/09/10 15:15
 * description
 */
class ForgotPasswordPresenter : BasePresenter<ForgotPasswordContract.View>(),ForgotPasswordContract.Presenter{
    private val forgotPasswordModel by lazy { ForgotPasswordModel() }
    override fun getSendSMSData(mobile: String, event: String) {
        checkViewAttached()
        val disposable = forgotPasswordModel.getSendSMSData(mobile, event)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<String>("发送验证码失败"){
                    override fun onNext(t: String) { mRootView!!.setSendSMSData() }
                    override fun onFail(e: ApiException) {  mRootView!!.showError(e.mMessage,e.errorCode) }
                })
        addSubscription(disposable)
    }
    override fun getResetPwdData(event: String,mobile: String, type: String, newpwd: String, captcha: String) {
        val disposable = forgotPasswordModel.getCheckVerificationCodeData(mobile, event, captcha)
                .doOnNext{
                    mRootView!!.setVerificationCodeData(it.message,it.code)
                }
                .flatMap{
                    if (it.code == ApiErrorCode.SUCCESS) {
                        forgotPasswordModel.getResetPwdData(mobile, type, newpwd, captcha)
                    } else {
                        Observable.error(ApiException(it.code,it.message))
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<String>("重置密码失败"){
                    override fun onFail(e: ApiException) {  mRootView!!.setResetPwdData(e.errorCode == ApiErrorCode.SUCCESS) }
                    override fun onNext(t: String) {  mRootView!!.setResetPwdData(true) }
                })
        addSubscription(disposable)

    }

}