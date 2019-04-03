package com.haidie.dangqun.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.OnlineHelpListData

/**
 * Create by   Administrator
 *      on     2018/09/12 16:05
 * description
 */
class OnlineHelpListAdapter(layoutResId: Int, data: MutableList<OnlineHelpListData.ListBean>?)
    : BaseQuickAdapter<OnlineHelpListData.ListBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: OnlineHelpListData.ListBean?) {
        helper!!.setText(R.id.tv_title, item!!.title)
        val status = item.status
//        帮扶状态,0未帮扶,1帮扶中,2已帮扶
        val textStatus = when (status) {
            0 ->  "未帮扶"
            1 ->   "帮扶中"
            else ->  "已帮扶"
        }
        helper.setText(R.id.tv_status, textStatus)
        helper.setText(R.id.tv_username, item.username)
        val troubletype = item.troubletype
//        困难类型 1.因病,2.因残,3.因学,4.因灾,5.因技术,6.缺劳动力,7.缺资金,8其他
        val troubleType = when (troubletype) {
            1 ->  "因病"
            2 ->  "因残"
            3 ->  "因学"
            4 ->  "因灾"
            5 ->  "因技术"
            6 ->  "缺劳动力"
            7 ->  "缺资金"
            else -> "8其他"
        }
        helper.setText(R.id.tv_trouble_type, troubleType)
        helper.setText(R.id.tv_worker, item.worker)
        helper.setText(R.id.tv_create_time, item.create_time)
    }
}