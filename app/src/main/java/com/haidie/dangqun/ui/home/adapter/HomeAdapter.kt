package com.haidie.dangqun.ui.home.adapter

import android.graphics.Typeface
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.Constants
import com.haidie.dangqun.MyApplication
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.HomeNewsData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Created by admin2
 *  on 2018/08/14  16:56
 * description
 */
class HomeAdapter(layoutResId: Int, data: MutableList<HomeNewsData.ListBean>?) :
        BaseQuickAdapter<HomeNewsData.ListBean, BaseViewHolder>(layoutResId, data) {
    private var textTypeFaceMedium: Typeface? = null
    private var textTypeFaceNormal: Typeface? = null

    init {
        textTypeFaceMedium = Typeface.createFromAsset(MyApplication.context.assets, Constants.FONTS_MEDIUM)
        textTypeFaceNormal = Typeface.createFromAsset(MyApplication.context.assets, Constants.FONTS_NORMAL)
    }

    override fun convert(helper: BaseViewHolder?, item: HomeNewsData.ListBean?) {
        val type = helper!!.getView<TextView>(R.id.tv_title_type)
        type.typeface = textTypeFaceMedium
        type.text = item!!.toutiao_article_type_name
        val title = helper.getView<TextView>(R.id.tv_title)
        title.typeface = textTypeFaceMedium
        title.text = item.title
        val time = helper.getView<TextView>(R.id.tv_update_time)
        time.typeface = textTypeFaceNormal
        time.text = item.update_time
        val pic = item.cover_one
        ImageLoader.load(mContext, pic, helper.getView(R.id.iv_cover_one))
    }
}