package com.haidie.dangqun.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.VolunteerListData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/09/11 17:29
 * description
 */
class VolunteerListAdapter(layoutResId: Int, data: MutableList<VolunteerListData.VolunteerListItemData>?)
    : BaseQuickAdapter<VolunteerListData.VolunteerListItemData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: VolunteerListData.VolunteerListItemData?) {
        val avatar = item!!.avator
        ImageLoader.loadCircle(mContext, avatar, helper!!.getView(R.id.iv_avator))
        val username = item.username
        helper.setText(R.id.tv_username, username)
        val isLeader = item.is_leader
        helper.setText(R.id.tv_is_leader, isLeader)
        val createTime = item.create_time
        helper.setText(R.id.tv_create_time, createTime)
    }
}