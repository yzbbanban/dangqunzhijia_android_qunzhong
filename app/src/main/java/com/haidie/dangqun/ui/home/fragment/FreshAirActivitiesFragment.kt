package com.haidie.dangqun.ui.home.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.home.FreshAirActivitiesContract
import com.haidie.dangqun.mvp.model.bean.FreshAirActivitiesData
import com.haidie.dangqun.mvp.presenter.home.FreshAirActivitiesPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.FreshAirActivitiesAdapter
import com.haidie.dangqun.ui.life.activity.LifeDetailActivity
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/10 13:51
 * description
 */
class FreshAirActivitiesFragment : BaseFragment(),FreshAirActivitiesContract.View {

    private val mPresenter by lazy { FreshAirActivitiesPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var isRefresh = false
    private var moduleType: Int = 1
    private var isRegisterCopy: Int = 0
    private lateinit var mData: MutableList<FreshAirActivitiesData.FreshAirActivitiesItemData>
    private lateinit var mAdapter: FreshAirActivitiesAdapter
    companion object {
        fun getInstance(position: Int): FreshAirActivitiesFragment {
            val fragment = FreshAirActivitiesFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.isRegisterCopy = position
            return fragment
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view

    override fun initView() {
        mPresenter.attachView(this)
        common_toolbar.visibility = View.GONE
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multiple_status_view
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun setRefresh() {
        smart_layout.setOnRefreshListener{
            page = 1
            isRefresh = true
            lazyLoad()
            it.finishRefresh(1000)
        }
        smart_layout.setOnLoadMoreListener{
            page++
            isRefresh = false
            lazyLoad()
            it.finishLoadMore(1000)
        }
    }

    private fun initRecyclerView() {
        mData = mutableListOf()
        mAdapter = FreshAirActivitiesAdapter(R.layout.layout_volunteer_activities_list_item, mData)
        mAdapter.setOnItemClickListener{ _, _, position ->
//            val intent = Intent(activity, FreshAirActivitiesDetailActivity::class.java)
//            intent.putExtra(Constants.ID,mAdapter.data[position].id)
//            /index/newwind/detail?id=${val.id}
//            startActivity(intent)
            val url = "/index/newwind/detail?id=${mAdapter.data[position].id}"
            LifeDetailActivity.startActivity(activity, url)
//            activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(activity))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
    }
    override fun lazyLoad() {
        mPresenter.getFreshAirActivitiesData(uid,token,page,Constants.SIZE,moduleType,isRegisterCopy)
    }
    private var isFirst: Boolean = true
    override fun setFreshAirActivitiesData(freshAirActivitiesData: FreshAirActivitiesData) {
        mData = freshAirActivitiesData.list
        when{
            isRefresh -> {
                isFirst = false
                if (mData.isEmpty()) {
                    showShort( "暂无数据内容")
                    mLayoutStatusView?.showEmpty()
                    smart_layout.isEnableLoadMore = false
                    smart_layout.isEnableRefresh = false
                    return
                }
                smart_layout.isEnableRefresh = true
                mAdapter.replaceData(mData)
                smart_layout.isEnableLoadMore = mData.size >= Constants.SIZE
            }
            else -> {
                if (mData.isNotEmpty()) {
                    mAdapter.addData(mData)
                    mAdapter.notifyDataSetChanged()
                    smart_layout.isEnableLoadMore = mData.size >= Constants.SIZE
                } else {
                    if (page > 1) page --
                    if (isFirst) {
                        mLayoutStatusView?.showEmpty()
                        smart_layout.isEnableRefresh = false
                        smart_layout.isEnableLoadMore = false
                        showShort( "暂无数据内容")
                    }else{
                        showShort(resources.getString(R.string.load_more_no_data))
                    }
                }
            }
        }
    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR ->   mLayoutStatusView?.showNoNetwork()
            else ->   mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}