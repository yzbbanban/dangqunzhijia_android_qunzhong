package com.haidie.dangqun.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.LivingPaymentData

/**
 * Create by   Administrator
 *      on     2018/08/29 13:11
 * description
 */
class LivingPaymentAdapter(layoutResId: Int, data: List<LivingPaymentData.LivingPaymentItemData>?) :
        BaseQuickAdapter<LivingPaymentData.LivingPaymentItemData, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: LivingPaymentData.LivingPaymentItemData?) {
        helper!!.getView<TextView>(R.id.tv_orderNo).text = item!!.orderNo
        helper.getView<TextView>(R.id.tvStopDate).text = "截止日期 ${item.stop_date}"
        helper.getView<TextView>(R.id.tvPowerFee).text = "能源费 ${item.power_fee}"
        helper.getView<TextView>(R.id.tvWuyeFee).text = "物业费 ${item.wuye_fee}"
        helper.getView<TextView>(R.id.tvNeedPay).text = item.need_pay
        val payStatus = item.pay_status
        helper.getView<TextView>(R.id.tvPayStatus).text = when (payStatus) {
            1 -> "已支付"
            else -> "去支付"
        }
        helper.addOnClickListener(R.id.tvNeedPay)
    }
}