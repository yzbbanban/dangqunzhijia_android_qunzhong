package com.haidie.dangqun.ui.business.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.CommodityClassificationListData


/**
 * Create by   Administrator
 *      on     2018/09/05 16:52
 * description
 */
class CommodityClassificationAdapter(layoutResId: Int, data: ArrayList<CommodityClassificationListData>)
    : BaseQuickAdapter<CommodityClassificationListData,BaseViewHolder>(layoutResId, data){
    override fun convert(helper: BaseViewHolder?, item: CommodityClassificationListData?) {
        helper!!.getView<TextView>(R.id.text_name).text = item!!.keyname
    }
}