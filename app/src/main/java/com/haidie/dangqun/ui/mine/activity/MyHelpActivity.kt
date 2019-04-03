package com.haidie.dangqun.ui.mine.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.ui.mine.fragment.MyHelpListFragment
import kotlinx.android.synthetic.main.activity_tab_layout_view_pager.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/10/22 08:39
 * description  我的求助
 */
class MyHelpActivity : BaseActivity() {
    private var mTabDataList = arrayListOf("未审核","已审核通过","未审核通过","已帮助")
    private val mFragments = ArrayList<BaseFragment>()
    override fun getLayoutId(): Int = R.layout.activity_tab_layout_view_pager

    override fun initData() {
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener{ onBackPressed() }
        tv_title.text = "我的求助"
    }

    override fun initView() {
        for (index in 0 until mTabDataList.size) {
            val fragment = MyHelpListFragment.getInstance(index)
            mFragments.add(fragment)
        }
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = mFragments[position]
            override fun getCount(): Int = mTabDataList.size
            override fun getPageTitle(position: Int): CharSequence = mTabDataList[position]
        }
        tabLayout.setViewPager(viewPager)
        viewPager.currentItem = 0
//       避免多次切换出现空白页面
        viewPager.offscreenPageLimit = mTabDataList.size
    }

    override fun start() {
    }
}