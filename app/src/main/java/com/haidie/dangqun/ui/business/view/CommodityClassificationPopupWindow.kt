package com.haidie.dangqun.ui.business.view

import android.annotation.SuppressLint
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.haidie.dangqun.R
import com.haidie.dangqun.mvp.model.bean.CommodityClassificationListData
import com.haidie.dangqun.ui.business.adapter.CommodityClassificationAdapter
import razerdp.basepopup.BasePopupWindow

/**
 * Create by   Administrator
 *      on     2018/09/05 16:45
 * description
 */
class CommodityClassificationPopupWindow(list: ArrayList<CommodityClassificationListData>, context: FragmentActivity) : BasePopupWindow(context) {

    init {
        setBackPressEnable(false)
        isDismissWhenTouchOuside = true
        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(CommodityClassificationRecyclerViewDividerItemDecoration(context))
        val adapter = CommodityClassificationAdapter(R.layout.commodity_classification_popup_window_item, list)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener {
            _, _, position ->
            listener!!.onClickListener(position)
        }
    }
    var listener: OnItemClickListener? = null
    interface OnItemClickListener{
        fun onClickListener(position : Int)
    }

    override fun getClickToDismissView(): View {
        return popupWindowView
    }

    override fun initShowAnimation(): Animation {
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0f)
        translateAnimation.duration = 500
        return translateAnimation
    }

    override fun initExitAnimation(): Animation {
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, -1.0f)
        translateAnimation.duration = 500
        return translateAnimation
    }

    override fun onCreatePopupView(): View {
        return createPopupById(R.layout.commodity_classification_popup_window)
    }

    @SuppressLint("WrongViewCast")
    override fun initAnimaView(): View {
        return findViewById(R.id.popup_animation)
    }
}