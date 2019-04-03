package com.haidie.dangqun.ui.home.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.ServicePhoneData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Created by admin2
 *  on 2018/08/18  16:07
 * description
 */
class ServicePhoneItemViewAdapter(layoutResId: Int, data: ArrayList<ServicePhoneData.GridItemBean>?) :
    BaseQuickAdapter<ServicePhoneData.GridItemBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: ServicePhoneData.GridItemBean?) {
        val iv = helper!!.getView<ImageView>(R.id.ivAvatar)
        ImageLoader.load(mContext, item!!.avatar, iv)
        helper.getView<TextView>(R.id.tvNickname).text = item.nickname
        helper.getView<TextView>(R.id.tvPosition).text = item.position
        helper.getView<TextView>(R.id.tvGroupName).text = item.groupName
        helper.getView<TextView>(R.id.tvMobile).text = item.mobile
    }
}