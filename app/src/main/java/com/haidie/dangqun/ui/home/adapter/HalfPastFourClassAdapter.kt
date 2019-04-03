package com.haidie.dangqun.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.HalfPastFourClassData
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/12/11 13:59
 * description
 */
class HalfPastFourClassAdapter (layoutResId: Int, data: MutableList<HalfPastFourClassData.HalfPastFourClassItemData>?)
    : BaseQuickAdapter<HalfPastFourClassData.HalfPastFourClassItemData, BaseViewHolder>(layoutResId, data){
    private var mType: Int = -1
    override fun convert(helper: BaseViewHolder?, item: HalfPastFourClassData.HalfPastFourClassItemData?) {
        var title = ""
        when (mType) {
            0 ->  title = item!!.cid
            1 ->  title = item!!.content
        }
        helper!!.setText(R.id.tvTitle, title)
        val startTime = item!!.start_time
        helper.setText(R.id.tvTime, DateUtils.getTimeToChina(startTime))
        val pic = item.pic
        ImageLoader.load(mContext,pic,helper.getView(R.id.ivPic))
    }
    fun setFridayChoir(type : Int){
        mType = type
    }
}