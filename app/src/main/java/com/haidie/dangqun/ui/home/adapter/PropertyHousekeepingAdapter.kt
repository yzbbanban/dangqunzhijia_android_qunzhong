package com.haidie.dangqun.ui.home.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R

/**
 * Create by   Administrator
 *      on     2018/09/07 16:20
 * description
 */
class PropertyHousekeepingAdapter(layoutResId: Int, data: MutableList<Map<String, Any>>?)
    : BaseQuickAdapter<Map<String, Any>, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: Map<String, Any>?) {
        val icon = item!![Constants.ICON] as Int
        if (-1 != icon) {
            Glide.with(mContext).load(icon).into(helper!!.getView(R.id.iv_image))
        } else {
            (helper!!.getView<ImageView>(R.id.iv_image)).setImageResource(R.drawable.icon_default)
        }
        helper.setText(R.id.tv_name, item[Constants.TEXT] as String)
    }
}