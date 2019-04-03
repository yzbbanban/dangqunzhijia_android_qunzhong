package com.haidie.dangqun.ui.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R

/**
 * Created by admin2
 *  on 2018/08/18  11:05
 * description
 */
class MyArticleAdapter(layoutResId: Int, data: MutableList<String>?)
    : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper!!.addOnClickListener(R.id.tv_edit)
        helper.addOnClickListener(R.id.tv_delete)
    }
}