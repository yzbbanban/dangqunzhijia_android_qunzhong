package com.haidie.dangqun.ui.main.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.main.ForgotPasswordContract
import com.haidie.dangqun.mvp.presenter.main.ForgotPasswordPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.utils.RegexUtils
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/09/10 15:11
 * description  忘记密码
 */
class ForgotPasswordActivity : BaseActivity(),ForgotPasswordContract.View {
    private val mPresenter by lazy { ForgotPasswordPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_forgot_password
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        tv_title.text = "忘记密码"
        iv_back.setOnClickListener { onBackPressed() }

        tv_send_verification_code.setOnClickListener {
            //            发送验证码
            val phone = et_phone.text.toString()
            if (phone.isEmpty()) {
                showShort("请输入手机号")
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(phone)) {
                showShort("手机格式错误")
                return@setOnClickListener
            }
//            调用发送验证码接口并且按钮置灰
//            发送验证码的事件,忘记密码的event=’resetpwd’,注册的event=’register’
            mPresenter.getSendSMSData(phone,Constants.RESET_PWD)
//            开始计时
            timer.start()
        }
        tv_confirm.setOnClickListener {
            CircularAnim.hide(tv_confirm)
                    .endRadius(progress_bar.height / 2f)
                    .go { progress_bar.visibility = View.VISIBLE }
            val mobile = et_phone.text.toString()
            val password = et_password.text.toString()
            val confirmPassword = et_confirm_password.text.toString()
            val verificationCode = et_verification_code.text.toString()
            when {
                mobile.isEmpty() -> {
                    showShort("请输入手机号码")
                    showConfirm()
                }
                password.isEmpty() -> {
                    showShort("请输入密码")
                    showConfirm()
                }
                confirmPassword.isEmpty() -> {
                    showShort("请再次输入密码")
                    showConfirm()
                }
                verificationCode.isEmpty() -> {
                    showShort("请输入验证码")
                    showConfirm()
                }
                password != confirmPassword -> {
                    showShort("密码不一致")
                    showConfirm()
                }
                !RegexUtils.isMobileSimple(mobile) -> {
                    showShort("手机格式错误")
                    showConfirm()
                }
                        //  发送验证码的事件,忘记密码的event=’resetpwd’,注册的event=’register’
                        //  调用短信验证码验证接口及找回忘记密码功能接口
                else ->  mPresenter.getResetPwdData(Constants.RESET_PWD,mobile,Constants.MOBILE,password,verificationCode)
            }
        }
    }
    private fun showConfirm() {
        CircularAnim.show(tv_confirm).go()
        progress_bar.visibility = View.GONE
    }
    private val timer = object : CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            tv_send_verification_code.isEnabled = false
            tv_send_verification_code.text = (millisUntilFinished / 1000).toString() + "秒后可重发"
            tv_send_verification_code.setTextColor(ContextCompat.getColor(this@ForgotPasswordActivity,R.color.background_color))
            tv_send_verification_code.background = ContextCompat.getDrawable(this@ForgotPasswordActivity,R.drawable.verification_text_bg_not_clickable)
        }
        override fun onFinish() {
            tv_send_verification_code.isEnabled = true
            tv_send_verification_code.text = "点击发送"
            tv_send_verification_code.setTextColor(ContextCompat.getColor(this@ForgotPasswordActivity,R.color.black))
            tv_send_verification_code.background = ContextCompat.getDrawable(this@ForgotPasswordActivity,R.drawable.verification_text_bg_normal)
            cancel()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}
    override fun setSendSMSData() { showShort("发送成功") }
    override fun showError(msg: String, errorCode: Int) {  showShort(msg)  }
    override fun setVerificationCodeData(msg: String, errorCode: Int) {
        if (errorCode != ApiErrorCode.SUCCESS) {
            showShort(msg)
            showConfirm()
        }
    }
    override fun setResetPwdData(isSuccess: Boolean) {
        if (isSuccess) {
            showShort("重置密码成功")
//            返回登录页面进行登录
            startActivity(Intent(this@ForgotPasswordActivity,LoginActivity::class.java))
        }else{
            showShort("重置密码失败")
            showConfirm()
        }
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}