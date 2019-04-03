package com.haidie.dangqun.ui.home.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.ServiceCategoryData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/09/10 11:00
 * description
 */
class ServiceCategoryAdapter(layoutResId: Int, data: MutableList<ServiceCategoryData.ServiceCategoryItemData>?)
    : BaseQuickAdapter<ServiceCategoryData.ServiceCategoryItemData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: ServiceCategoryData.ServiceCategoryItemData?) {
        val image = item!!.image
        if (image.isNotEmpty()) {
            ImageLoader.load(mContext, image, helper!!.getView(R.id.iv_image))
        } else {
            (helper!!.getView<ImageView>(R.id.iv_image)).setImageResource(R.drawable.icon_default)
        }
        helper.setText(R.id.tv_name, item.name)
    }
}