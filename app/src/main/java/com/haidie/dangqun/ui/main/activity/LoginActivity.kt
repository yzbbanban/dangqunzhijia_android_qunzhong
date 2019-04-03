package com.haidie.dangqun.ui.main.activity

import android.content.Intent
import android.view.KeyEvent
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.main.LoginContract
import com.haidie.dangqun.mvp.event.RegisterXGEvent
import com.haidie.dangqun.mvp.model.bean.RegisterData
import com.haidie.dangqun.mvp.presenter.main.LoginPresenter
import com.haidie.dangqun.net.exception.ApiException
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.utils.ActivityCollector
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.utils.RegexUtils
import com.haidie.dangqun.utils.StatusBarUtil
import com.tencent.android.tpush.XGPushConfig
import kotlinx.android.synthetic.main.activity_login.*
import top.wefor.circularanim.CircularAnim

/**
 * Created by admin2
 *  on 2018/08/20  16:09
 * description  登录页面
 */
class LoginActivity : BaseActivity(), LoginContract.View {

    private val mPresenter by lazy { LoginPresenter() }
    private var loginAccount:String by Preference(Constants.ACCOUNT,Constants.EMPTY_STRING)
    private var loginPassword:String by Preference(Constants.PASSWORD,Constants.EMPTY_STRING)
    private var deviceId by Preference(Constants.DEVICE_ID,Constants.EMPTY_STRING)
    private val groupId: String = "1"
    private val deviceType: String = "android"
    override fun getLayoutId(): Int = R.layout.activity_login
    override fun initData() {
        et_mobile.setText(loginAccount)
        et_mobile.setSelection(loginAccount.length)
        et_password.setText(loginPassword)
        et_password.setSelection(loginPassword.length)
        deviceId = XGPushConfig.getToken(this)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun initView() {
        mPresenter.attachView(this)
        StatusBarUtil.immersive(this)
        //  跳转到注册页面
        tv_register.setOnClickListener { startActivity(Intent(this@LoginActivity,RegisterActivity::class.java)) }
        // 跳转到忘记密码页面
        tv_forgot_password.setOnClickListener { startActivity(Intent(this@LoginActivity,ForgotPasswordActivity::class.java)) }
        tv_login.setOnClickListener {
            CircularAnim.hide(tv_login)
                    .endRadius(progress_bar.height / 2f)
                    .go { progress_bar.visibility = View.VISIBLE }
            val mobile = et_mobile.text.toString()
            val password = et_password.text.toString()
            when {
                mobile.isEmpty() -> {
                    showShort("请输入手机号码")
                    showLogin()
                }
                password.isEmpty() -> {
                    showShort("请输入密码")
                    showLogin()
                }
                !RegexUtils.isMobileSimple(mobile) -> {
                    showShort("手机格式错误")
                    showLogin()
                }
                else ->  mPresenter.getLoginData(mobile,groupId, password,deviceId,deviceType)
            }
        }
    }
    private fun showLogin() {
        CircularAnim.show(tv_login).go()
        progress_bar.visibility = View.GONE
    }
    override fun start() {}
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityCollector.instance.exitApp()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    override fun autoLogin(mobile : String, password : String) {
        et_mobile.setText(mobile)
        et_mobile.setSelection(mobile.length)
        et_password.setText(password)
        et_password.setSelection(password.length)
        mPresenter.getLoginData(mobile, groupId,password,deviceId,deviceType)
    }
    override fun setLoginData(registerData: RegisterData) {
        RxBus.getDefault().post(RegisterXGEvent())
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
    override fun loginFailed(e: ApiException) {
        showShort(e.mMessage)
        showLogin()
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}