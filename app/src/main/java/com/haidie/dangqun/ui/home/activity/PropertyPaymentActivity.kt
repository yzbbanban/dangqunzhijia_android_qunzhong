package com.haidie.dangqun.ui.home.activity

import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.home.view.PayPopupWindow
import com.haidie.dangqun.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_property_payment.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/26 20:11
 * description  物业缴费
 */
class PropertyPaymentActivity : BaseActivity() {
    private var  payPopupWindow : PayPopupWindow? = null
    override fun getLayoutId(): Int = R.layout.activity_property_payment

    override fun initData() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "物业缴费"
    }

    override fun initView() {
        payPopupWindow = PayPopupWindow(this@PropertyPaymentActivity,multipleStatusView)
        tvImmediatePayment.setOnClickListener {
            //弹出支付选择方式
            if (!payPopupWindow!!.isShowing) {
                payPopupWindow?.apply {
                    showPopupWindow()
                    setSelectedWeiChat()
                }
            }
        }
        payPopupWindow?.setPayListener(object : PayPopupWindow.OnPayClickListener{
            override fun paymentMethod(code: Int) {
                when (code) {
                    Constants.PAYMENT_METHOD_WEI_CHAT -> {
                        if (!CommonUtils.isWeChatAppInstalled(this@PropertyPaymentActivity)) {
                            showShort("未安装微信应用")
                            payPopupWindow?.showToPay()
                            return
                        }
//                      调用微信支付预付订单接口
//                        mPresenter.getPrepaidOrderData(uid,token,orderNo,needPay,"$year-${month}月份-物业费账单")
                    }
                    Constants.PAYMENT_METHOD_ALI_PAY -> {
                        showShort("支付宝暂未开通")
                        payPopupWindow?.showToPay()
                    }
                }
            }
        })
    }
    override fun start() {
    }
}