package com.haidie.dangqun.ui.mine.activity

import android.support.v4.app.Fragment
import android.view.View
import com.flyco.tablayout.listener.OnTabSelectListener
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.mine.fragment.IParticipatedFragment
import com.haidie.dangqun.ui.mine.fragment.IReleasedFragment
import kotlinx.android.synthetic.main.activity_my_order.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/14 15:11
 * description  我的活动
 */
class MyActivityActivity : BaseActivity() {
    private var mFragments: ArrayList<Fragment>? = null
    private val mTitles = arrayOf("我参加的", "我发布的")
    override fun getLayoutId(): Int = R.layout.activity_my_order
    override fun initData() {}
    override fun initView() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "我的活动"
        iv_add.visibility = View.VISIBLE
            // 跳转到发布活动页面
        iv_add.setOnClickListener { toActivity(ReleaseActivitiesActivity::class.java) }
    }

    override fun start() {
        mFragments = ArrayList()
        val iParticipatedFragment = IParticipatedFragment.getInstance(0, null)
        val iReleasedFragment = IReleasedFragment.getInstance(1, null)
        mFragments!!.add(iParticipatedFragment)
        mFragments!!.add(iReleasedFragment)
        tab_layout.setTabData(mTitles, this, R.id.frame_layout, mFragments)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                if (position == 1) {
                    (mFragments!![position] as IReleasedFragment).setBoolean(true)
                }
            }
            override fun onTabReselect(position: Int) {}
        })
    }
}