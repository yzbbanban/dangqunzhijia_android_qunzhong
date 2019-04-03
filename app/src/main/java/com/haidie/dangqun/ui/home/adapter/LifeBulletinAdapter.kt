package com.haidie.dangqun.ui.home.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.ArticleListData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/09/10 08:22
 * description
 */
class LifeBulletinAdapter(layoutResId: Int, data: MutableList<ArticleListData.ArticleListItemData>?)
    : BaseQuickAdapter<ArticleListData.ArticleListItemData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: ArticleListData.ArticleListItemData?) {
        helper!!.setText(R.id.tvTitle, item!!.title)
        helper.setText(R.id.tvTime, item.create_time)

        if (item.pic.isNotEmpty()) {
            ImageLoader.load(mContext, item.pic, helper.getView(R.id.ivPic))
        } else {
            (helper.getView<ImageView>(R.id.ivPic)).setImageResource(R.drawable.icon_default)
        }
    }
}