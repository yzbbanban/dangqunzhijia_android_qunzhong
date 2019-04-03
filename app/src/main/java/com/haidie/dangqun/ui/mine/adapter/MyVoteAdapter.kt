package com.haidie.dangqun.ui.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.MyVoteListData

/**
 * Create by   Administrator
 *      on     2018/09/17 15:09
 * description
 */
class MyVoteAdapter(layoutResId: Int, data: MutableList<MyVoteListData.ListBean>?)
    : BaseQuickAdapter<MyVoteListData.ListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: MyVoteListData.ListBean?) {
        helper!!.setText(R.id.tv_title, item!!.title)
        helper.setText(R.id.tv_type, if (item.type == 0) "单选" else "多选")
        helper.setText(R.id.tv_create_time, item.create_time)
    }
}