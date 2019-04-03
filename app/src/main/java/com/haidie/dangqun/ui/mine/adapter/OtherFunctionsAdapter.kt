package com.haidie.dangqun.ui.mine.adapter

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.utils.DisplayManager


/**
 * Created by admin2
 *  on 2018/08/11  13:49
 * description
 */
class OtherFunctionsAdapter(layoutResId: Int, data: MutableList<String>?) :
        BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    private var textTypeFaceRegular: Typeface? = null

    init { textTypeFaceRegular = Typeface.createFromAsset(MyApplication.context.assets,  Constants.FONTS_REGULAR) }

    private val icons = arrayOf(R.drawable.about_us,R.drawable.logout,R.drawable.change_password)

    override fun convert(helper: BaseViewHolder?, item: String?) {
        val textView = helper!!.getView<TextView>(R.id.tv_title)
        val drawable = ContextCompat.getDrawable(mContext,icons[helper.layoutPosition])
        drawable.setBounds(0, 0, DisplayManager.dip2px(20f)!!,DisplayManager.dip2px(20f)!!)
        textView.setCompoundDrawables(drawable, null, null, null)
        textView.text = item
        textView.typeface = textTypeFaceRegular
    }
}