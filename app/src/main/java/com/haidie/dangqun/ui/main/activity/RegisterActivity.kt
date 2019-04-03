package com.haidie.dangqun.ui.main.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.main.RegisterContract
import com.haidie.dangqun.mvp.event.AutoLoginEvent
import com.haidie.dangqun.mvp.model.bean.RegisterData
import com.haidie.dangqun.mvp.presenter.main.RegisterPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.main.view.GroupPopupWindow
import com.haidie.dangqun.utils.DisplayManager
import com.haidie.dangqun.utils.RegexUtils
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/09/07 13:25
 * description  注册页面
 */
class RegisterActivity : BaseActivity(), RegisterContract.View {

    private val mPresenter by lazy { RegisterPresenter() }
    private var groupId : Int? = 1
    private var phone: String? = null
    private var pwd: String? = null
    override fun getLayoutId(): Int = R.layout.activity_register
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        tv_title.text = "注册"
        iv_back.setOnClickListener { onBackPressed() }

        val groupPopupWindow = GroupPopupWindow(this@RegisterActivity,linear_layout)
        tv_group.setOnClickListener {
            tv_group.requestFocus()
            et_mobile.clearFocus()
            et_password.clearFocus()
            et_ensure_password.clearFocus()
            closeKeyboard(et_mobile, this@RegisterActivity)
            if (!groupPopupWindow.isShowing) {
                groupPopupWindow.showPopupWindow()
            }
        }
        groupPopupWindow.groupListener = object : GroupPopupWindow.OnGroupClickListener {
            override fun group(group: String) {
                tv_group.text = group
                groupId = 1
            }
        }

        et_verification_code.setOnFocusChangeListener {
            _, hasFocus ->
            val params = view_line.layoutParams
            if (hasFocus) {
                view_line.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
                params.height = DisplayManager.dip2px(2f)!!
                view_line.layoutParams = params
            }else{
                view_line.setBackgroundColor(ContextCompat.getColor(this,R.color.edit_text_line_default_color))
                params.height = DisplayManager.dip2px(1f)!!
                view_line.layoutParams = params
            }
        }
        tv_send_verification_code.setOnClickListener {
            //            发送验证码
            val phone = et_mobile.text.toString()
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
            mPresenter.getSendSMSData(phone, Constants.REGISTER)
//            开始计时
            timer.start()
        }
        tv_register.setOnClickListener(View.OnClickListener {
            CircularAnim.hide(tv_register)
                    .endRadius((progress_bar.height / 2).toFloat())
                    .go({ progress_bar.visibility = View.VISIBLE })
            val mobile = et_mobile.text.toString()
            if (mobile.isEmpty()) {
                showShort("请输入手机号码")
                showRegister()
                return@OnClickListener
            }
            val verificationCode = et_verification_code.text.toString()
            if (verificationCode.isEmpty()) {
                showShort("请输入验证码")
                showRegister()
                return@OnClickListener
            }
            val password = et_password.text.toString()
            if (password.isEmpty()) {
                showShort("请输入密码")
                showRegister()
                return@OnClickListener
            }
            val ensurePassword = et_ensure_password.text.toString()
            if (ensurePassword.isEmpty()) {
                showShort("请输入确认密码")
                showRegister()
                return@OnClickListener
            }
            if (!cb_choose.isChecked) {
                showShort("请同意用户使用协议")
                showRegister()
                return@OnClickListener
            }
            if (!RegexUtils.isMobileSimple(mobile)) {
                showShort("手机格式错误")
                showRegister()
                return@OnClickListener
            }
            if (password != ensurePassword) {
                showShort("密码不一致")
                showRegister()
                return@OnClickListener
            }
            phone = mobile
            pwd = password
            //  发送验证码的事件,忘记密码的event=’resetpwd’,注册的event=’register’
            //调用短信验证码验证接口及注册接口
            mPresenter.getRegisterData(Constants.REGISTER,mobile,et_nickname.text.toString(),groupId, password,verificationCode)
        })
    }
    private val timer = object : CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            tv_send_verification_code.isEnabled = false
            tv_send_verification_code.text = (millisUntilFinished / 1000).toString() + "秒后可重发"
            tv_send_verification_code.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.background_color))
            tv_send_verification_code.background = ContextCompat.getDrawable(this@RegisterActivity,R.drawable.verification_text_bg_not_clickable)
        }
        override fun onFinish() {
            tv_send_verification_code.isEnabled = true
            tv_send_verification_code.text = "点击发送"
            tv_send_verification_code.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.black))
            tv_send_verification_code.background = ContextCompat.getDrawable(this@RegisterActivity,R.drawable.verification_text_bg_normal)
            cancel()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun showRegister() {
        CircularAnim.show(tv_register).go()
        progress_bar.visibility = View.GONE
    }
    override fun start() {}
    override fun setSendSMSData() {
        showShort("发送成功")
    }
    override fun setVerificationCodeData(msg: String, errorCode: Int) {
        if (errorCode != ApiErrorCode.SUCCESS) {
            showShort(msg)
            showRegister()
        }
    }
    override fun setRegisterData(registerData: RegisterData) {
        showShort("注册成功")
//        发送自动登录事件
        RxBus.getDefault().post(AutoLoginEvent(phone!!,pwd!!))
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        showRegister()
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}