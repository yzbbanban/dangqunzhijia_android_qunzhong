package com.haidie.dangqun.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.haidie.dangqun.R
import kotlinx.android.synthetic.main.release_dialog.*

/**
 * Created by admin2
 *  on 2018/08/14  13:52
 * description  发布弹窗
 */
class ReleaseDialog(context: Context?, themeResId: Int) : Dialog(context, themeResId) {

    private var mHandler: Handler? = null
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //全屏
        val params = window!!.attributes
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        window!!.attributes = params
    }
    init {
        mContext = context
        mHandler = Handler()
        setContentView(R.layout.release_dialog)
        iv_exit!!.setOnClickListener {
            outDia()
        }
        relative_layout!!.setOnClickListener {
            outDia()
        }
    }

    override fun show() {
        super.show()
        goInDia()
    }

    private fun goInDia() {
        ll_product!!.visibility = View.INVISIBLE
        ll_article!!.visibility = View.INVISIBLE
        //首先把控件设置为不可见
        relative_layout!!.animation = AnimationUtils.loadAnimation(mContext, R.anim.main_go_in)
        //然后设置动画
        iv_exit!!.animation = AnimationUtils.loadAnimation(mContext, R.anim.main_rotate_right)
        //这里设置底部退出按钮的动画 这里是用了一个rotate动画
        ll_product!!.visibility = View.VISIBLE
        //底部按钮动画执行过之后把发布设置为可见
        ll_product!!.animation = AnimationUtils.loadAnimation(mContext, R.anim.main_shoot_in)
        //然后让他执行main_shoot_in动画这个动画里定义的是平移动画
        //在这里设置之后如果你同时设置其他两个评估和回收动画着这三个动画会同时从屏幕的底部向上平移
        //而我们想实现的效果是挨个向上平移这里 使用到了定时器handler开启一个线程定时100毫秒启动这个线程
        // 这样就可以达到挨个向上平移的效果
        // mHandler.postDelayed开启一个定时任务

        mHandler!!.postDelayed({
            ll_article!!.visibility = View.VISIBLE
            ll_article!!.animation = AnimationUtils.loadAnimation(mContext, R.anim.main_shoot_in)
        }, 200)
        //这里需要设置成两百不然会出现和评估同时向上滑动
    }

    private fun outDia() {
        relative_layout!!.animation = AnimationUtils.loadAnimation(mContext, R.anim.main_go_out)

        iv_exit!!.animation = AnimationUtils.loadAnimation(mContext, R.anim.main_rotate_left)
        //设置退出按钮从右向左旋转
        mHandler!!.postDelayed({ dismiss() }, 500)
        //这里设置了一个定时500毫秒的定时器来执行dismiss();来关闭Dialog 我们需要在500毫秒的时间内完成对控件动画的设置
        ll_product!!.animation = AnimationUtils.loadAnimation(mContext, R.anim.main_shoot_out)
        //然后设置发布从上向下平移动画
        ll_product!!.visibility = View.INVISIBLE
        //将其设置为不可见

        //同理使用定时器将评估和回向下平移 这里需要注意的是评估和回收的定时器时间的设置不能大于关闭Dialog的定时时间
        mHandler!!.postDelayed({
            ll_article!!.animation = AnimationUtils.loadAnimation(mContext, R.anim.main_shoot_out)
            ll_article!!.visibility = View.INVISIBLE
        }, 150)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (isShowing) {
            outDia()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    fun setProductClickListener(clickListener: View.OnClickListener): ReleaseDialog {
        ll_product!!.setOnClickListener(clickListener)
        return this
    }
    fun setArticleClickListener(clickListener: View.OnClickListener): ReleaseDialog {
        ll_article!!.setOnClickListener(clickListener)
        return this
    }
}