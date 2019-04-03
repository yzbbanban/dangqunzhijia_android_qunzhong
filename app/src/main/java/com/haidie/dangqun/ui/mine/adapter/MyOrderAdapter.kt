package com.haidie.dangqun.ui.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.MyOrderListData

/**
 * Create by   Administrator
 *      on     2018/09/14 09:39
 * description
 */
class MyOrderAdapter(layoutResId: Int, data: MutableList<MyOrderListData.ListBean>?)
    : BaseQuickAdapter<MyOrderListData.ListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: MyOrderListData.ListBean?) {
        helper!!.setText(R.id.tv_title, item!!.title)
        helper.setText(R.id.tv_status, item.status)
        helper.setText(R.id.tv_orderNo, item.orderNo)
        helper.setText(R.id.tv_time, item.time)
    }
}