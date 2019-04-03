package com.haidie.dangqun.ui.mine.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.OnlineHelpListData
import com.haidie.dangqun.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/10/22 09:52
 * description
 */
class MyHelpListAdapter(layoutResId: Int ,data: MutableList<OnlineHelpListData.ListBean>?)
    : BaseQuickAdapter<OnlineHelpListData.ListBean, BaseViewHolder>(layoutResId,data)  {
    private var submitAgainShow = false
    private var editShow = false
    private var deleteShow = false
    override fun convert(helper: BaseViewHolder?, item: OnlineHelpListData.ListBean?) {
        helper!!.setText(R.id.tvTitle,item!!.title)
        helper.setText(R.id.tvContent, item.content)
        val pic1 = item.pic1
        if (pic1.isNotEmpty()) {
            ImageLoader.load(mContext,pic1,helper.getView(R.id.ivPic1))
        }else{
            helper.getView<ImageView>(R.id.ivPic1).setImageResource(R.drawable.icon_default)
        }
        if (submitAgainShow) helper.getView<TextView>(R.id.tvSubmitAgain).visibility = View.VISIBLE
        if (editShow) helper.getView<TextView>(R.id.tvEdit).visibility = View.VISIBLE
        if (deleteShow) {
            helper.getView<LinearLayout>(R.id.llUnaudited).visibility = View.VISIBLE
            helper.getView<TextView>(R.id.tvDelete).visibility = View.VISIBLE
        }
        helper.addOnClickListener(R.id.tvSubmitAgain)
        helper.addOnClickListener(R.id.tvEdit)
        helper.addOnClickListener(R.id.tvDelete)
    }
    fun setSubmitAgainVisibility(isSubmitAgainShow : Boolean){
        submitAgainShow = isSubmitAgainShow
    }
    fun setEditVisibility(isEditShow : Boolean){
        editShow = isEditShow
    }
    fun setDeleteVisibility(isDeleteShow : Boolean){
        deleteShow = isDeleteShow
    }
}