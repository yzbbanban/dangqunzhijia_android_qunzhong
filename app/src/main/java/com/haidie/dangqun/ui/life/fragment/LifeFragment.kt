package com.haidie.dangqun.ui.life.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.life.LifeContract
import com.haidie.dangqun.mvp.presenter.life.LifePresenter
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_life.*

/**
 * Created by admin2
 *  on 2018/08/13  19:52
 * description  生活
 */
class LifeFragment : BaseFragment(), LifeContract.View {

    private val mPresenter by lazy { LifePresenter() }
    private var mTitle: String? = null
    private var mFragments = arrayListOf<BaseFragment>()
    private var isRefresh = false
//    private var mTabDataList = arrayListOf("关注","综合","党建","政务","物业","监管","新风","互助","关爱")
//    关注，小区广场，社区新闻，参观
    private var mTabDataList = arrayListOf("关注","小区广场","社区新闻","参观")
    companion object {
        fun getInstance(title: String): LifeFragment {
            val fragment = LifeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }
    override fun getLayoutId(): Int = R.layout.fragment_life
    override fun initView() {
        tv_title.text = mTitle
        mPresenter.attachView(this)
        mLayoutStatusView = multipleStatusView
        initViewPagerAndTabLayout()
    }
    override fun reloadLife() {
        mFragments.clear()
        viewPagerLife.post {
            initViewPagerAndTabLayout()
        }
    }
    override fun lazyLoad() {}
    private fun initViewPagerAndTabLayout() {
        mTabDataList.forEachIndexed { index, _ ->
            val fragment = LifeListFragment.getInstance(index)
            mFragments.add(fragment)
        }
        viewPagerLife.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment = mFragments[position]
            override fun getCount(): Int = mTabDataList.size
            override fun getPageTitle(position: Int): CharSequence? = mTabDataList[position]
        }
        viewPagerLife.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    (mFragments[position] as LifeListFragment).lazyLoad()
                }
            }
        })
        tabLayoutLife.setViewPager(viewPagerLife)
        viewPagerLife.currentItem = 1
        viewPagerLife.offscreenPageLimit = mTabDataList.size
    }
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() { mLayoutStatusView?.showContent() }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}