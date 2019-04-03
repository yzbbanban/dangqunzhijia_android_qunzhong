package com.haidie.dangqun.ui.home.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.ServiceListData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/09/10 11:49
 * description
 */
class ServiceListAdapter(layoutResId: Int, data: MutableList<ServiceListData.ServiceListItemData>?)
    : BaseQuickAdapter<ServiceListData.ServiceListItemData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: ServiceListData.ServiceListItemData?) {
        if (item!!.pic.isNotEmpty()) {
            ImageLoader.load(mContext, item.pic, helper!!.getView(R.id.iv_pic))
        } else {
            (helper!!.getView<ImageView>(R.id.iv_pic)).setImageResource(R.drawable.icon_default)
        }
        helper.setText(R.id.tv_title, item.title)
        helper.setText(R.id.tv_area, item.area)
        helper.setText(R.id.tv_bus_name, item.bus_name)
        helper.addOnClickListener(R.id.iv_phone)
    }
}