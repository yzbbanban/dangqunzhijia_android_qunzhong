package com.haidie.dangqun.ui.home.adapter

import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.LifePaymentData
import com.haidie.dangqun.utils.DisplayManager

/**
 * Create by   Administrator
 *      on     2018/11/26 18:31
 * description
 */
class LifePaymentAdapter(layoutResId: Int, data: List<LifePaymentData>)
    : BaseQuickAdapter<LifePaymentData, BaseViewHolder>(layoutResId, data){
    override fun convert(helper: BaseViewHolder?, item: LifePaymentData?) {
        val textView = helper!!.getView<TextView>(R.id.tvTitle)
        textView?.text = item!!.title
        val drawable = ContextCompat.getDrawable(mContext,item.icon)
        drawable.setBounds(0, 0, DisplayManager.dip2px(20f)!!,DisplayManager.dip2px(20f)!!)
        textView.setCompoundDrawables(drawable, null, null, null)
        helper.getView<ImageView>(R.id.ivBg).background = ContextCompat.getDrawable(mContext,item.color)
    }
}