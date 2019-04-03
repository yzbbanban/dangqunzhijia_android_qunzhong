package com.haidie.dangqun.ui.home.adapter

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.ServicePhoneData
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration

/**
 * Created by admin2
 *  on 2018/08/18  15:59
 * description
 */
class ServicePhoneRecyclerViewAdapter(layoutResId: Int, data: ArrayList<ServicePhoneData>?) :
    BaseQuickAdapter<ServicePhoneData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: ServicePhoneData?) {
        val textView = helper!!.getView<TextView>(R.id.tvTitle)
        textView.text = item!!.title
        val itemBean = item.list
        val recyclerView = helper.getView<RecyclerView>(R.id.recyclerViewItem)
        val itemViewAdapter = ServicePhoneItemViewAdapter(R.layout.service_phone_item_view_item, itemBean)
        recyclerView.let {
            it.layoutManager = LinearLayoutManager(mContext)
            it.addItemDecoration(RecyclerViewDividerItemDecoration(mContext))
            it.adapter = itemViewAdapter
        }
        helper.setNestView(R.id.llRecyclerItemGroup)
        helper.addOnClickListener(R.id.recyclerViewItem)
        itemViewAdapter.setOnItemClickListener { _, _, position ->
            val mobile = itemBean!![position].mobile
            if (mobile.isNotEmpty()) {
                call(mobile)
            }
        }
    }
    private fun call(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mContext.startActivity(intent)
    }
}