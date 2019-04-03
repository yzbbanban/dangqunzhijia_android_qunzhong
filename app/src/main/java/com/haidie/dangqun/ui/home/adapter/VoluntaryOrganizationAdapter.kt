package com.haidie.dangqun.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.VoluntaryOrganizationListData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/09/11 15:43
 * description
 */
class VoluntaryOrganizationAdapter(layoutResId: Int, data: MutableList<VoluntaryOrganizationListData.VoluntaryOrganizationListItemData>?)
    : BaseQuickAdapter<VoluntaryOrganizationListData.VoluntaryOrganizationListItemData, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: VoluntaryOrganizationListData.VoluntaryOrganizationListItemData?) {
        val pic = item!!.pic
        ImageLoader.load(mContext, pic, helper!!.getView(R.id.iv_pic))
        val title = item.title
        helper.setText(R.id.tv_title, title)
        val createTime = item.create_time
        helper.setText(R.id.tv_create_time, createTime)
        val man = item.man
        helper.setText(R.id.tv_man, "" + man)
        val woman = item.woman
        helper.setText(R.id.tv_woman, "" + woman)
    }
}