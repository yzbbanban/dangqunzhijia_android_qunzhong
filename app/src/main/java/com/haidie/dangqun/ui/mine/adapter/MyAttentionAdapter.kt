package com.haidie.dangqun.ui.mine.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R

/**
 * Created by admin2
 *  on 2018/08/18  10:09
 * description
 */
class MyAttentionAdapter(layoutResId: Int, data: MutableList<String>?)
    : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        (helper!!.getView<TextView>(R.id.tv_name)).text = item
        helper.addOnClickListener(R.id.iv_avatar)
    }
}