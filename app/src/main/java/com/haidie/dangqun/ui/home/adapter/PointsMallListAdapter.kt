package com.haidie.dangqun.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.PointsMallListData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/09/11 09:50
 * description
 */
class PointsMallListAdapter(layoutResId: Int, data: MutableList<PointsMallListData.ListBean>?)
    : BaseQuickAdapter<PointsMallListData.ListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: PointsMallListData.ListBean?) {
        val pic = item!!.pic
        ImageLoader.load(mContext, pic, helper!!.getView(R.id.iv_pic))
        val title = item.title
        helper.setText(R.id.tv_title, title)
        val score = item.score
        helper.setText(R.id.tv_score,"${score}积分")
        helper.addOnClickListener(R.id.tv_exchange)
    }
}