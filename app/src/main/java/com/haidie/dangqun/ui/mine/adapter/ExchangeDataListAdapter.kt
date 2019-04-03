package com.haidie.dangqun.ui.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.ExchangeDataListData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/09/13 16:17
 * description
 */
class ExchangeDataListAdapter(layoutResId: Int, data: MutableList<ExchangeDataListData.ListBean>?)
    : BaseQuickAdapter<ExchangeDataListData.ListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: ExchangeDataListData.ListBean?) {
        val pic = item!!.pic
        ImageLoader.load(mContext, pic, helper!!.getView(R.id.iv_pic))
        helper.setText(R.id.tv_title, item.title)
        helper.setText(R.id.tv_status, item.status)
        helper.setText(R.id.tv_orderNo, item.orderNo)
        helper.setText(R.id.tv_phone, item.phone)
        helper.setText(R.id.tv_user_score, "消耗积分:" + item.user_score)
        helper.setText(R.id.tv_create_time, item.create_time)
    }
}