package com.haidie.dangqun.ui.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import razerdp.basepopup.BasePopupWindow
import top.wefor.circularanim.CircularAnim


/**
 * Create by   Administrator
 *      on     2018/09/04 09:30
 * description
 */
class PayPopupWindow(activity: BaseActivity,viewGroup: ViewGroup) : BasePopupWindow(activity),View.OnClickListener {
    private var popupView: View? = null
    private var view = viewGroup
    private var cbWeiChat: CheckBox? = null
    private var cbAliPay: CheckBox? = null
    private var progressBar: ProgressBar? = null
    private var tvToPay: TextView? = null
    private var paymentMethod = Constants.NEGATIVE_ONE
    private var mActivity = activity

    init {
        init()
        bindEvent()
    }

    override fun initShowAnimation(): Animation {
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0f)
        translateAnimation.duration = 500
       return translateAnimation
    }

    override fun initExitAnimation(): Animation {
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1.0f)
        translateAnimation.duration = 500
        return translateAnimation
    }
    override fun onCreatePopupView(): View {
        popupView = LayoutInflater.from(context).inflate(R.layout.pay_popup_window, view,false)
        return popupView!!
    }

    override fun initAnimaView(): View = popupView!!.findViewById(R.id.popup_anim)
    override fun getClickToDismissView(): View = popupView!!.findViewById(R.id.click_to_dismiss)
    private fun init() {
        cbWeiChat = popupView!!.findViewById(R.id.cb_wei_chat)
        cbAliPay = popupView!!.findViewById(R.id.cb_ali_pay)
        progressBar = popupView!!.findViewById(R.id.progress_bar)
        tvToPay = popupView!!.findViewById(R.id.tv_to_pay)

        cbWeiChat!!.setOnCheckedChangeListener {
            _, isCheck ->
            cbAliPay!!.isChecked = false
            cbWeiChat!!.isChecked = isCheck
            paymentMethod = if (isCheck)  Constants.PAYMENT_METHOD_WEI_CHAT else Constants.NEGATIVE_ONE
        }
        cbAliPay!!.setOnCheckedChangeListener {
            _, isCheck ->
            cbWeiChat!!.isChecked = false
            cbAliPay!!.isChecked = isCheck
            paymentMethod = if (isCheck)  Constants.PAYMENT_METHOD_ALI_PAY else Constants.NEGATIVE_ONE
        }
    }
    private fun bindEvent() {
        if (popupView != null) {
            popupView!!.findViewById<View>(R.id.ic_close).setOnClickListener(this)
            popupView!!.findViewById<View>(R.id.ll_we_chat).setOnClickListener(this)
            popupView!!.findViewById<View>(R.id.ll_ali_pay).setOnClickListener(this)
            tvToPay!!.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ic_close -> dismiss()

            R.id.ll_we_chat -> {
                cbAliPay!!.isChecked = false
                cbWeiChat!!.isChecked = !cbWeiChat!!.isChecked
            }
            R.id.ll_ali_pay -> {
                cbWeiChat!!.isChecked = false
                cbAliPay!!.isChecked = !cbAliPay!!.isChecked
            }
            R.id.tv_to_pay -> {
                CircularAnim.hide(tvToPay)
                        .endRadius(progressBar!!.height / 2f)
                        .go { progressBar!!.visibility = View.VISIBLE }
                when (paymentMethod){
                    Constants.NEGATIVE_ONE -> {
                        mActivity.showShort("请选择支付方式")
                        showToPay()
                    }
                    else -> payListener.paymentMethod(paymentMethod)
                }
            }
        }
    }
    fun showToPay() {
        CircularAnim.show(tvToPay).go()
        progressBar!!.visibility = View.GONE
    }
    private lateinit var payListener: OnPayClickListener
    interface OnPayClickListener{
        fun paymentMethod(code : Int)
    }
    fun setPayListener(onPayClickListener: OnPayClickListener){
        payListener = onPayClickListener
    }

    /**
     * 默认选择微信支付
     */
    fun setSelectedWeiChat() {
        paymentMethod = Constants.PAYMENT_METHOD_WEI_CHAT
        cbWeiChat!!.isChecked = true
    }
}