package com.haidie.dangqun.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.OrderListData

/**
 * Create by   Administrator
 *      on     2018/09/08 10:00
 * description
 */
class WorkOrderManagementAdapter(layoutResId: Int, data: MutableList<OrderListData.OrderListItemData>?)
    : BaseQuickAdapter<OrderListData.OrderListItemData, BaseViewHolder>(layoutResId, data) {
    private var groupId: Int = Constants.NEGATIVE_ONE
    override fun convert(helper: BaseViewHolder?, item: OrderListData.OrderListItemData?) {
        val title = item!!.title
        helper!!.setText(R.id.tv_title, title)
        val status = item.status
        helper.setText(R.id.tv_status, status)
        val workerGroup = item.worker_group
        if (workerGroup != null) {
            helper.setText(R.id.tv_worker_group_text, "受理部门")
            helper.setText(R.id.tv_worker_group_value, workerGroup)
        }
        val proposeOrAccept: String = when (groupId) {
            1 -> "我提出的"
            else -> "我受理的"
        }
        helper.setText(R.id.tv_group_id, proposeOrAccept)
        val isChange = item.is_change
        if (isChange != null) {
            helper.setText(R.id.tv_is_change, isChange)
        }
        val username = item.username
        helper.setText(R.id.tv_username, username)
        val category = item.category
        helper.setText(R.id.tv_category, category)
        val createTime = item.create_time
        helper.setText(R.id.tv_create_time, createTime)
    }

    fun setGroupId(group_id: Int) {
        groupId = group_id
    }
}