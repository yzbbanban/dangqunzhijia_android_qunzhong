package com.haidie.dangqun.ui.main.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import com.haidie.dangqun.R
import razerdp.basepopup.BasePopupWindow


/**
 * Create by   Administrator
 *      on     2018/09/04 09:30
 * description
 */
class GroupPopupWindow(context: Context,viewGroup: ViewGroup) : BasePopupWindow(context),View.OnClickListener {
    private var popupView: View? = null
    private var view = viewGroup
    private var tvGroup: TextView? = null
    init {
        tvGroup = popupView!!.findViewById(R.id.tv_group)
        tvGroup!!.setOnClickListener(this)
        popupView!!.findViewById<View>(R.id.tv_cancel).setOnClickListener(this)
    }
    override fun initShowAnimation(): Animation {
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0f)
        translateAnimation.duration = 500
       return translateAnimation
    }
    override fun initExitAnimation(): Animation {
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1.0f)
        translateAnimation.duration = 500
        return translateAnimation
    }
    override fun onCreatePopupView(): View {
        popupView = LayoutInflater.from(context).inflate(R.layout.group_popup_window, view,false)
        return popupView!!
    }
    override fun initAnimaView(): View = popupView!!.findViewById(R.id.popup_anima)
    override fun getClickToDismissView(): View = popupView!!.findViewById(R.id.click_to_dismiss)

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_group -> {
                groupListener.group(tvGroup!!.text.toString())
                dismiss()
            }
        }
    }
    lateinit var groupListener: OnGroupClickListener
    interface OnGroupClickListener{
        fun group(group : String)
    }
}