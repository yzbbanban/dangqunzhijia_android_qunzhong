package com.haidie.dangqun.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.HistoricalBillData

/**
 * Create by   Administrator
 *      on     2018/08/30 16:53
 * description
 */
class HistoricalBillAdapter(layoutResId: Int, data: List<HistoricalBillData.HistoricalBillItemData>?)
    : BaseQuickAdapter<HistoricalBillData.HistoricalBillItemData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: HistoricalBillData.HistoricalBillItemData?) {
        helper!!.getView<TextView>(R.id.tv_orderNo).text = item!!.orderNo
        helper.getView<TextView>(R.id.tv_year_month).text = "${item.year}/${item.month}"
        helper.getView<TextView>(R.id.tv_username).text = item.username
        helper.getView<TextView>(R.id.tv_need_pay).text = item.need_pay.toString()
        helper.getView<TextView>(R.id.tv_create_time).text = item.create_time
        val payStatusText = helper.getView<TextView>(R.id.tv_pay_status)
        val payStatus = item.pay_status
//        付款状态:0表示未支付,1表示已支付
        when (payStatus) {
            0 ->  payStatusText.text = "未支付"
            else ->  payStatusText.text = "已支付"
        }
    }
}