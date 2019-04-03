package com.haidie.dangqun.mvp.presenter.main

import com.haidie.dangqun.Constants
import com.haidie.dangqun.base.BaseObserver
import com.haidie.dangqun.base.BasePresenter
import com.haidie.dangqun.mvp.contract.main.LoginContract
import com.haidie.dangqun.mvp.event.AutoLoginEvent
import com.haidie.dangqun.mvp.model.bean.RegisterData
import com.haidie.dangqun.mvp.model.main.LoginModel
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.rx.RxUtils
import com.haidie.dangqun.utils.Preference


/**
 * Created by admin2
 *  on 2018/08/22  13:10
 * description
 */
class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {
    private val loginModel by lazy { LoginModel() }
    private var loginAccount by Preference(Constants.ACCOUNT, Constants.EMPTY_STRING)
    private var loginPassword by Preference(Constants.PASSWORD, Constants.EMPTY_STRING)
    private var loginStatus by Preference(Constants.LOGIN_STATUS, Constants.DEFAULT_FALSE)
    private var username by Preference(Constants.USERNAME, Constants.EMPTY_STRING)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var avatar by Preference(Constants.AVATAR, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    override fun attachView(mRootView: LoginContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscription(RxBus.getDefault().toFlowable(AutoLoginEvent::class.java)
                .subscribe{
                    mRootView!!.autoLogin(it.mobile,it.password)
                })
    }

    override fun getLoginData(user: String,group_id: String, password: String,device_id: String, device_type: String) {
        checkViewAttached()
        val disposable = loginModel.getLoginData(user,group_id, password, device_id, device_type)
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<RegisterData>("登录失败") {
                    override fun onNext(data: RegisterData) {
                        loginAccount = data.userinfo.mobile
                        loginPassword = password
                        loginStatus = true
                        token = data.userinfo.token
                        uid = data.userinfo.user_id
                        username = data.userinfo.username
                        avatar = data.userinfo.avatar
                        mRootView!!.setLoginData(data)
                    }
                    override fun onFail(e: ApiException) {  mRootView!!.loginFailed(e) }
                })
        addSubscription(disposable)
    }
}