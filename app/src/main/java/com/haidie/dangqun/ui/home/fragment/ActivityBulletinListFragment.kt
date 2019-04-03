package com.haidie.dangqun.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.home.VolunteerActivitiesListContract
import com.haidie.dangqun.mvp.model.bean.MyVolunteerActivitiesListData
import com.haidie.dangqun.mvp.model.bean.VolunteerActivitiesListData
import com.haidie.dangqun.mvp.presenter.home.VolunteerActivitiesListPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.VolunteerActivitiesListAdapter
import com.haidie.dangqun.ui.mine.activity.MyVolunteerActivitiesDetailActivity
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/10/25 10:01
 * description
 */
open class ActivityBulletinListFragment : BaseFragment(), VolunteerActivitiesListContract.View  {


    private val mPresenter by lazy { VolunteerActivitiesListPresenter() }

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var status: Int = 0     //0报名中,1已结束,2进行中
    private var isRefresh = false
    private var isSignUp: Boolean? = null
    private var mType: Int = 3      //  传入活动类型(0默认,1党务活动,2党员活动，3志愿者活动)
    private lateinit var mData: MutableList<MyVolunteerActivitiesListData.VolunteerActivitiesListItemData>
    private lateinit var mAdapter: VolunteerActivitiesListAdapter
    companion object {
        fun getInstance(position: Int): ActivityBulletinListFragment {
            val fragment = ActivityBulletinListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            when (position) {
                0 -> fragment.status = 0
                1 -> fragment.status = 2
                2 -> fragment.status = 1
            }
            return fragment
        }
    }
    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view

    override fun initView() {
        mPresenter.attachView(this)
        common_toolbar.visibility = View.GONE
        isSignUp = status == 0
        setRefresh()
        initRecyclerView()
        mLayoutStatusView = multiple_status_view
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
        mData = ArrayList()
        mAdapter = VolunteerActivitiesListAdapter(R.layout.layout_volunteer_activities_list_item, mData)
        mAdapter.setOnItemClickListener{ _, _, position ->
            val intent = Intent(activity, MyVolunteerActivitiesDetailActivity::class.java)
            intent.putExtra(Constants.ID,mAdapter.data[position].id)
            intent.putExtra(Constants.IS_SIGN_UP,isSignUp)
            startActivity(intent)
            activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }

        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(activity))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun lazyLoad() {
        mPresenter.getVolunteerActivitiesListData(uid,null,page,Constants.SIZE,mType,status,token)
    }
    private var isFirst: Boolean = true
    override fun setMyVolunteerActivitiesListData(myVolunteerActivitiesListData: MyVolunteerActivitiesListData) {
        mData = myVolunteerActivitiesListData.list
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
    override fun setVolunteerActivitiesListData(volunteerActivitiesListData: VolunteerActivitiesListData) {
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