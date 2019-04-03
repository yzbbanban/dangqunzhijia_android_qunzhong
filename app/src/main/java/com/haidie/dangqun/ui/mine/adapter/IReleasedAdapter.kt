package com.haidie.dangqun.ui.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.IReleasedData

/**
 * Create by   Administrator
 *      on     2018/09/14 16:15
 * description
 */
class IReleasedAdapter(layoutResId: Int, data: MutableList<IReleasedData.ListBean>?)
    : BaseQuickAdapter<IReleasedData.ListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: IReleasedData.ListBean?) {
        helper!!.setText(R.id.tv_title, item!!.title)
//        报名状态0已报名,1已取消
        helper.setText(R.id.tv_status, if (item.status == 0) "已报名" else "已取消")
//        活动类型:1.党务活动,2党员活动
        helper.setText(R.id.tv_type, if (item.type == 1) "党务活动" else "党员活动")
        helper.setText(R.id.tv_create_time, item.create_time)
    }
}