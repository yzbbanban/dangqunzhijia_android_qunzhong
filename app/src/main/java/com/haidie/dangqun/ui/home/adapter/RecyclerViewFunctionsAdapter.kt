package com.haidie.dangqun.ui.home.adapter

import android.graphics.Typeface
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.glide.GlideApp

/**
 * Created by admin2
 *  on 2018/08/17  14:29
 * description
 */
class RecyclerViewFunctionsAdapter(layoutResId: Int, data: MutableList<Map<String, Any>>?)
    : BaseQuickAdapter<Map<String, Any>, BaseViewHolder>(layoutResId, data) {
    private var textTypeface: Typeface? = null
    init {
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets, Constants.FONTS_REGULAR)
    }
    override fun convert(helper: BaseViewHolder?, item: Map<String, Any>?) {
        val icon = item!![Constants.ICON] as Int
        GlideApp.with(mContext)
                .load(icon)
                .into(helper!!.getView(R.id.iv_image))
        (helper.getView<TextView>(R.id.tv_name)).text = item[Constants.TEXT] as String
        (helper.getView<TextView>(R.id.tv_name)).typeface = textTypeface
    }
}