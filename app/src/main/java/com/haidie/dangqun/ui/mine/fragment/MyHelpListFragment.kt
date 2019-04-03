package com.haidie.dangqun.ui.mine.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.mine.MyHelpListContract
import com.haidie.dangqun.mvp.model.bean.OnlineHelpListData
import com.haidie.dangqun.mvp.presenter.mine.MyHelpListPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.mine.activity.MyHelpDetailActivity
import com.haidie.dangqun.ui.mine.activity.MyHelpEditActivity
import com.haidie.dangqun.ui.mine.adapter.MyHelpListAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/10/22 09:00
 * description
 */
class MyHelpListFragment : BaseFragment(),MyHelpListContract.View {

    private val mPresenter by lazy { MyHelpListPresenter() }
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var index: Int? = null
    private var checkStatus = 0
    private var status = 0
    private val unaudited = 0
    private val approved = 1
    private val unapproved = 2
    private val hasHelped = 3
    private var page: Int = 1
    private var isRefresh = false
    private val mId  = 1      //上报类型,填1,表示网上求助
    private var isHelp: Boolean = false
    private lateinit var mAdapter: MyHelpListAdapter
    private lateinit var mData: MutableList<OnlineHelpListData.ListBean>

    companion object {
        fun getInstance(position: Int): MyHelpListFragment {
            val fragment = MyHelpListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.index = position
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
        smart_layout.setOnRefreshListener {
            page = 1
            isRefresh = true
            lazyLoad()
            it.finishRefresh(1000)
        }
        smart_layout.setOnLoadMoreListener {
            page++
            isRefresh = false
            lazyLoad()
            it.finishLoadMore(1000)
        }
    }

    private fun initRecyclerView() {
        mData = ArrayList()
        mAdapter = MyHelpListAdapter(R.layout.layout_my_help_list_item,mData)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(activity))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
        mAdapter.replaceData(mData)
        when (index!!) {
            unaudited -> {
                isHelp = false
                checkStatus = 0
                status = 0
                mAdapter.setSubmitAgainVisibility(true)
                mAdapter.setEditVisibility(true)
                mAdapter.setDeleteVisibility(true)
            }
            approved -> {
                isHelp = true
                checkStatus = 1
                status = 0
            }
            unapproved -> {
                isHelp = false
                checkStatus = 2
                status = 0
            }
            hasHelped -> {
                isHelp = true
                checkStatus = 1
                status = 2
            }
        }
        mAdapter.setOnItemClickListener { _, _, position ->
//            跳转到详情页面
            MyHelpDetailActivity.startActivity(activity,mAdapter.data[position].id,isHelp)
        }
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.tvSubmitAgain -> {
                    //  调用网上求助再次提交接口  再次提交的状态,值填0
                    mPresenter.getOnlineHelpSubmitAgainData(uid,token,mAdapter.data[position].id,0)
                }
                R.id.tvDelete -> {
                    //调用网上求助删除接口  软删除状态,值填1,表示软删除
                    mPresenter.getOnlineHelpDeleteData(uid,token,mAdapter.data[position].id,1)
                }
                R.id.tvEdit -> {
                    MyHelpEditActivity.startActivity(activity,mAdapter.data[position].id)
                }
            }
        }
    }
    override fun lazyLoad() {
        mPresenter.getOnlineHelpData(uid,page,Constants.SIZE,mId,checkStatus,status,token)
    }
    private var isFirst: Boolean = true
    override fun setMyHelpListData(onlineHelpListData: OnlineHelpListData) {
        mData = onlineHelpListData.list
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
    override fun reloadMyHelp() {
        if (index == unaudited){
            smart_layout.autoRefresh()
        }
    }
    override fun setSubmitAgainData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("提交成功")
//            刷新当前页面数据
            smart_layout.autoRefresh()
        }else{
            showShort("提交失败")
        }
    }
    override fun setDeleteData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("删除成功")
//            刷新当前页面数据
            smart_layout.autoRefresh()
        }else{
            showShort("删除失败")
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
        when (errorCode) {
            ApiErrorCode.NETWORK_ERROR -> mLayoutStatusView?.showNoNetwork()
            else -> mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        if (!isRefresh) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() { mLayoutStatusView?.showContent() }
}