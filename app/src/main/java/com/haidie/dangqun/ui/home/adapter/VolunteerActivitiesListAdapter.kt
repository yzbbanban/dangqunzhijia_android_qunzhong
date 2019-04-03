package com.haidie.dangqun.ui.home.adapter

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.MyVolunteerActivitiesListData
import com.haidie.dangqun.utils.DateUtils
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/09/11 13:09
 * description
 */
class VolunteerActivitiesListAdapter(layoutResId: Int, data: MutableList<MyVolunteerActivitiesListData.VolunteerActivitiesListItemData>?)
    : BaseQuickAdapter<MyVolunteerActivitiesListData.VolunteerActivitiesListItemData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: MyVolunteerActivitiesListData.VolunteerActivitiesListItemData?) {
        val title = item!!.title
        val spannableStringBuilder = SpannableStringBuilder("$title  已报名")
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.RED),spannableStringBuilder.length-3,spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        helper!!.setText(R.id.tv_title, if (hasSignUp) spannableStringBuilder else title)
        val area = item.area
        val address = item.address
        helper.setText(R.id.tv_area_address, if (area == null ) address else area + address)
        val startTime = item.start_time
        val endTime = item.end_time
        helper.setText(R.id.tv_time, DateUtils.getTimeToChina(startTime) + "-" + DateUtils.getTimeToChina(endTime))
        val pic = item.pic
        ImageLoader.load(mContext,pic,helper.getView(R.id.ivPic))
    }

    private var hasSignUp: Boolean = false

    fun setSignUp(isSignUp : Boolean){
        hasSignUp = isSignUp
    }
}