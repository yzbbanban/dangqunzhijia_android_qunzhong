package com.haidie.dangqun.ui.home.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.ui.home.fragment.ActivityBulletinListFragment
import kotlinx.android.synthetic.main.activity_tab_layout_view_pager.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/10/25 09:57
 * description  活动公告
 */
class ActivityBulletinActivity : BaseActivity() {
    private var mTabDataList = arrayListOf("报名中","进行中","已结束")
    private val mFragments = ArrayList<BaseFragment>()
    override fun getLayoutId(): Int = R.layout.activity_tab_layout_view_pager

    override fun initData() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{ onBackPressed() }
        tv_title.text = "活动公告"
    }

    override fun initView() {
        for (index in 0 until mTabDataList.size) {
            val fragment = ActivityBulletinListFragment.getInstance(index)
            mFragments.add(fragment)
        }
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = mFragments[position]
            override fun getCount(): Int = mTabDataList.size
            override fun getPageTitle(position: Int): CharSequence = mTabDataList[position]
        }
        tabLayout.setViewPager(viewPager)
        viewPager.currentItem = 0
        viewPager.offscreenPageLimit = mTabDataList.size
    }

    override fun start() {
    }
}