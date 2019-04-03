package com.haidie.dangqun.ui.home.adapter

import android.graphics.Typeface
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.AllFunctionsDataBean

/**
 * Created by admin2
 *  on 2018/08/18  16:07
 * description
 */
class AllFunctionsGridViewAdapter(layoutResId: Int, data: ArrayList<AllFunctionsDataBean.GridItemBean>?)
    : BaseQuickAdapter<AllFunctionsDataBean.GridItemBean, BaseViewHolder>(layoutResId, data) {
    private var textTypeFaceRegular: Typeface? = null

    init {  textTypeFaceRegular = Typeface.createFromAsset(MyApplication.context.assets, Constants.FONTS_REGULAR) }

    override fun convert(helper: BaseViewHolder?, item: AllFunctionsDataBean.GridItemBean?) {
        Glide.with(mContext)
                .load(item!!.imageUrl)
                .into(helper!!.getView(R.id.iv_image))
        val textView = helper.getView<TextView>(R.id.tv_desc)
        textView.text = item.desc
        textView.typeface = textTypeFaceRegular
    }
}