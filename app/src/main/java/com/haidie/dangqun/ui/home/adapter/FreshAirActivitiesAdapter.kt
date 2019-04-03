package com.haidie.dangqun.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.FreshAirActivitiesData
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/12/10 14:31
 * description
 */
class FreshAirActivitiesAdapter(layoutResId: Int, data: MutableList<FreshAirActivitiesData.FreshAirActivitiesItemData>?)
    : BaseQuickAdapter<FreshAirActivitiesData.FreshAirActivitiesItemData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: FreshAirActivitiesData.FreshAirActivitiesItemData?) {
        val title = item!!.title

        helper!!.setText(R.id.tv_title, title)
        val address = item.address
        helper.setText(R.id.tv_area_address, address)
        val startTime = item.start_time
        val endTime = item.end_time
        if (startTime != null && endTime != null) {
            helper.setText(R.id.tv_time, DateUtils.getTimeToChina(startTime) + "-" + DateUtils.getTimeToChina(endTime))
        }
        val pic = item.cover_one
        ImageLoader.load(mContext,pic,helper.getView(R.id.ivPic))
    }
}