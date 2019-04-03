package com.haidie.dangqun.ui.home.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.ui.home.fragment.CaringActivitiesListFragment
import com.haidie.dangqun.ui.home.fragment.FreshAirActivitiesFragment
import kotlinx.android.synthetic.main.activity_tab_layout_view_pager.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/28 11:42
 * description  活动公告  新风活动+关爱活动
 */
class EventAnnouncementsActivity : BaseActivity(){
    private var mTabDataList = arrayListOf("新风活动","关爱活动")
    private val mFragments = ArrayList<BaseFragment>()
    override fun getLayoutId(): Int = R.layout.activity_tab_layout_view_pager
    override fun initData() {}
    override fun initView() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{ onBackPressed() }
        tv_title.text = "活动公告"
        val fragment = FreshAirActivitiesFragment.getInstance(0)
        val caringFragment = CaringActivitiesListFragment.getInstance(0)
        mFragments.add(fragment)
        mFragments.add(caringFragment)
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = mFragments[position]
            override fun getCount(): Int = mTabDataList.size
            override fun getPageTitle(position: Int): CharSequence = mTabDataList[position]
        }
        tabLayout.setViewPager(viewPager)
        viewPager.currentItem = 0
    }
    override fun start() {}
}