package com.haidie.dangqun.ui.home.activity

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.BillDetailContract
import com.haidie.dangqun.mvp.event.PayResultEvent
import com.haidie.dangqun.mvp.model.bean.BillDetailData
import com.haidie.dangqun.mvp.model.bean.PayResultData
import com.haidie.dangqun.mvp.model.bean.PrepaidOrderData
import com.haidie.dangqun.mvp.presenter.home.BillDetailPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.rx.RxBus
import com.haidie.dangqun.ui.home.view.PayPopupWindow
import com.haidie.dangqun.utils.CommonUtils
import com.haidie.dangqun.utils.DisplayManager
import com.haidie.dangqun.utils.Preference
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_bill_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/08/29 15:05
 * description  应收账单详情
 */
class BillDetailActivity : BaseActivity(), BillDetailContract.View{

    private val mPresenter by lazy { BillDetailPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var id = Constants.NEGATIVE_ONE
    private lateinit var orderNo: String
    private var  payPopupWindow : PayPopupWindow? = null
    private var api: IWXAPI? = null
    override fun getLayoutId(): Int = R.layout.activity_bill_detail
    override fun initData() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "缴费"
//        tv_submit.visibility = View.VISIBLE
//        tv_submit.text = "管理"
        id = intent.getIntExtra(Constants.ID, Constants.NEGATIVE_ONE)
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID,false)
//        将该app注册到微信
        api!!.registerApp(Constants.APP_ID)
    }
    override fun initView() {
        mPresenter.attachView(this)
        mLayoutStatusView = multiple_status_view

        payPopupWindow = PayPopupWindow(this@BillDetailActivity,multiple_status_view)

        tv_immediate_payment.setOnClickListener { _ ->
            //弹出支付选择方式
            if (!payPopupWindow!!.isShowing) {
                payPopupWindow?.let {
                    it.showPopupWindow()
                    it.setSelectedWeiChat()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

//    避免随意点击时重复加载
    private var isLoad: Boolean = true
    override fun start() {
        if (isLoad){
            mPresenter.getBillDetailData(uid,id,token)
        }
    }
    override fun showLoading() { mLayoutStatusView?.showLoading() }
    override fun dismissLoading() {
        isLoad = false
        mLayoutStatusView?.showContent()
    }
    @SuppressLint("SetTextI18n")
    override fun setBillDetailData(billDetailData: BillDetailData) {
        tv_start_date.text = "计费开始日期：${billDetailData.start_date}"
        tv_stop_date.text = "计费截止日期：${billDetailData.stop_date}"
        val needPay = billDetailData.need_pay
        tv_need_pay.text = "${needPay}元"
        orderNo = billDetailData.orderNo
        tv_orderNo.text = billDetailData.orderNo
        tvPowerFee.text = billDetailData.power_fee
        tvWuyeFee.text = billDetailData.wuye_fee
        tvNeedPay.text = billDetailData.need_pay
        tvMsg.text = billDetailData.msg
        val payStatus = billDetailData.pay_status
//        付款状态:0表示未支付,1表示已支付
        when (payStatus) {
            0 ->  {
                tvPayStatus.text = "未支付"
                frame_layout.visibility = View.VISIBLE
                val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
                params.setMargins(0,0,0, DisplayManager.dip2px(56f)!!)
                linear_layout.layoutParams = params
            }
            else -> {
                tvPayStatus.text = "已支付"
                frame_layout.visibility = View.GONE
                val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
                params.setMargins(0,0,0,0)
                linear_layout.layoutParams = params
            }
        }
        val pType = billDetailData.pay_type
        var payMethod = Constants.EMPTY_STRING
        if (pType != null){
            val payType = pType.toInt()
//        付款方式,0,支付宝,1银行转账,2微信,3线下付款
            when (payType) {
                0 -> payMethod = "支付宝"
                1 -> payMethod = "银行转账"
                2 -> payMethod = "微信"
                3 -> payMethod = "线下付款"
            }
        }
        tvPayType.text = payMethod
        tvThirdNo.text = billDetailData.thirdNo
        tvAuthor.text = billDetailData.author
        tvPayTime.text = billDetailData.pay_time
        tvCreateTime.text = billDetailData.create_time
        payPopupWindow!!.setPayListener(object : PayPopupWindow.OnPayClickListener{
            override fun paymentMethod(code: Int) {
                when (code) {
                    Constants.PAYMENT_METHOD_WEI_CHAT -> {
                        if (!CommonUtils.isWeChatAppInstalled(this@BillDetailActivity)) {
                            showShort("未安装微信应用")
                            payPopupWindow!!.showToPay()
                            return
                        }
//                      调用微信支付预付订单接口
                        mPresenter.getPrepaidOrderData(uid,token,orderNo,needPay.toString(),"本期物业费账单")
                    }
                    Constants.PAYMENT_METHOD_ALI_PAY -> {
                        showShort("支付宝暂未开通")
                        payPopupWindow!!.showToPay()
                    }
                }
            }
        })
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        isLoad = true
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR ->  mLayoutStatusView?.showNoNetwork()
            else ->   mLayoutStatusView?.showError()
        }
    }
    override fun showPayError(msg: String, errorCode: Int) {
        showShort(msg)
        payPopupWindow!!.showToPay()
    }
    override fun setPrepaidOrderData(prepaidOrderDate: PrepaidOrderData) {
        val appId = prepaidOrderDate.order_arr!!.appid
        val partnerId = prepaidOrderDate.order_arr.partnerid
        val prepayId = prepaidOrderDate.order_arr.prepayid
        val packages = prepaidOrderDate.order_arr.packages
        val nonceStr = prepaidOrderDate.order_arr.noncestr
        val timestamp = prepaidOrderDate.order_arr.timestamp
        val sign = prepaidOrderDate.order_arr.sign
        //data  根据服务器返回的json数据创建的实体类对象
        val req = PayReq()
        req.appId = appId
        req.partnerId = partnerId
        req.prepayId = prepayId
        req.packageValue = packages
        req.nonceStr = nonceStr
        req.timeStamp = timestamp
        req.sign = sign
        //发起请求调起支付
        api!!.sendReq(req)
    }
    override fun showPayEvent(isSuccess : Boolean) {
        when (isSuccess) {
            false -> {
                showShort("支付失败")
                payPopupWindow!!.showToPay()
            }
                     //调用异步获取微信支付结果通知比对结果是否一致
            true ->  mPresenter.getPayResult(uid,token,orderNo)
        }
    }
    override fun setPayResult(payResultData: PayResultData) {
        payPopupWindow!!.dismiss()
        showShort("支付成功")
        RxBus.getDefault().post(PayResultEvent())
        //刷新页面
        isLoad = true
        start()
    }
}