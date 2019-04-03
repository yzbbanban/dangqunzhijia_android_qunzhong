package com.haidie.dangqun.ui.home.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.BoundPaymentAccountListData

/**
 * Create by   Administrator
 *      on     2018/11/26 18:31
 * description
 */
class PaymentAccountAdapter(layoutResId: Int, data: MutableList<BoundPaymentAccountListData>)
    : BaseQuickAdapter<BoundPaymentAccountListData, BaseViewHolder>(layoutResId, data){
    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder?, item: BoundPaymentAccountListData?) {
        val spaceUnitHouse = helper!!.getView<TextView>(R.id.tvSpaceUnitHouse)
        spaceUnitHouse.text = "${item!!.space}栋${item.unit}单元${item.house}"
        val username = helper.getView<TextView>(R.id.tvUsername)
        username.text = item.username
        val phone = helper.getView<TextView>(R.id.tvPhone)
        phone.text = item.phone
        helper.addOnClickListener(R.id.tvUnbind)
    }
}