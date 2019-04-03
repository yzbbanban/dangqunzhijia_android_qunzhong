package com.haidie.dangqun.ui.home.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.ui.home.fragment.GovernmentArticleListFragment
import kotlinx.android.synthetic.main.activity_tab_layout_view_pager.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/13 13:39
 * description  三务公开列表
 */
class ThreeAffairsListActivity : BaseActivity() {
    private var mTabDataList = arrayOf("党务","村务","财务")
    private var ids = arrayOf("64", "65", "66")
    private val mFragments = ArrayList<BaseFragment>()
    override fun getLayoutId(): Int = R.layout.activity_tab_layout_view_pager
    override fun initData() {

        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "三务公开"
    }
    override fun initView() {

        for (index in 0 until mTabDataList.size) {
            val fragment = GovernmentArticleListFragment.getInstance(ids[index])
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

    override fun start() {}
}