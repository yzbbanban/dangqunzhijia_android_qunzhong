package com.haidie.dangqun.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.EvaluationListData

/**
 * Create by   Administrator
 *      on     2018/09/12 13:50
 * description
 */
class EvaluationListAdapter(layoutResId: Int, data: MutableList<EvaluationListData.ListBean>?)
    : BaseQuickAdapter<EvaluationListData.ListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: EvaluationListData.ListBean?) {
        helper!!.setText(R.id.tv_title, item!!.title)
        helper.setText(R.id.tv_num,  "${item.num}")
        helper.setText(R.id.tv_view,  "${item.view}")
        helper.setText(R.id.tv_time, item.start_time.split(" ")[0] +
                "～" + item.end_time.split(" ")[0])
        helper.setText(R.id.tv_create_time, item.create_time.split(" ")[0])
    }
}