package com.haidie.dangqun.mvp.presenter.mine

import com.haidie.dangqun.Constants
import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.mine.ChangePasswordContract
import com.haidie.dangqun.mvp.model.mine.ChangePasswordModel
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxUtils
import com.haidie.dangqun.utils.Preference

/**
 * Create by   Administrator
 *      on     2018/08/28 10:59
 * description
 */
class ChangePasswordPresenter : BasePresenter<ChangePasswordContract.View>(), ChangePasswordContract.Presenter {

    private val changePasswordModel by lazy { ChangePasswordModel() }
    private var loginPassword: String by Preference(Constants.PASSWORD, Constants.EMPTY_STRING)
    private var loginStatus: Boolean by Preference(Constants.LOGIN_STATUS, Constants.DEFAULT_FALSE)
    override fun getChangePasswordData(uid: Int, token: String, oldpwd: String, newpwd: String, repwd: String) {
        checkViewAttached()
        val disposable = changePasswordModel.getChangePasswordData(uid, token, oldpwd, newpwd, repwd)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<String>("修改失败"){
                    override fun onNext(t: String) {
                        mRootView!!.apply {
                            loginStatus = false
                            changePasswordSuccess()
                            loginPassword = newpwd
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView!!.apply {
                            if (e.errorCode == ApiErrorCode.SUCCESS) {
                                loginStatus = false
                                changePasswordSuccess()
                                loginPassword = newpwd
                            } else {
                                showError(e.mMessage, e.errorCode)
                            }
                        }
                    }
                })
        addSubscription(disposable)
    }
}