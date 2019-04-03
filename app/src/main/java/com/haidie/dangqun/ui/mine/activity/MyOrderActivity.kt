package com.haidie.dangqun.ui.mine.activity

import android.support.v4.app.Fragment
import android.view.View
import com.flyco.tablayout.listener.OnTabSelectListener
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.ui.mine.fragment.MyOrderFragment
import com.haidie.dangqun.ui.mine.fragment.MyOrderPropertyFragment
import kotlinx.android.synthetic.main.activity_my_order.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/13 17:07
 * description  我的订单
 */
class MyOrderActivity : BaseActivity() {
    private var mFragments: ArrayList<Fragment>? = null
    private val mTitles = arrayOf("家政服务", "物业服务")
    override fun getLayoutId(): Int = R.layout.activity_my_order
    override fun initData() {}
    override fun initView() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        tv_title.text = "我的订单"
    }

    override fun start() {
        mFragments = ArrayList()
        val myOrderFragment = MyOrderFragment.getInstance(1, null)
        val myOrderPropertyFragment = MyOrderPropertyFragment.getInstance(2, null)
        mFragments!!.add(myOrderFragment)
        mFragments!!.add(myOrderPropertyFragment)
        tab_layout.setTabData(mTitles, this, R.id.frame_layout, mFragments)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener{
            override fun onTabSelect(position: Int) {
                if (position == 1) {
                    (mFragments!![position] as MyOrderPropertyFragment).setBoolean(true)
                }
            }
            override fun onTabReselect(position: Int) { }
        })
    }
}