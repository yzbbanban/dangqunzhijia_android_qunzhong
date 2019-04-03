package com.haidie.dangqun.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.VolunteersParticipateActivitiesListData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/09/11 14:19
 * description
 */
class VolunteersParticipateActivitiesAdapter(layoutResId: Int, data: MutableList<VolunteersParticipateActivitiesListData.ListBean>?)
    : BaseQuickAdapter<VolunteersParticipateActivitiesListData.ListBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: VolunteersParticipateActivitiesListData.ListBean?) {
        val avatar = item!!.avator
        ImageLoader.loadCircle(mContext, avatar, helper!!.getView(R.id.iv_avator))
        val name = item.name
        helper.setText(R.id.tv_username, name)
        val phone = item.phone
        helper.setText(R.id.tv_phone, phone)
    }
}