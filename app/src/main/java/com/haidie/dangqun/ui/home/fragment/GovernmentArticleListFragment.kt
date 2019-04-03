package com.haidie.dangqun.ui.home.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseFragment
import com.haidie.dangqun.mvp.contract.home.GovernmentArticleListContract
import com.haidie.dangqun.mvp.model.bean.GovernmentArticleListData
import com.haidie.dangqun.mvp.presenter.home.GovernmentArticleListPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.activity.LifeBulletinDetailActivity
import com.haidie.dangqun.ui.home.adapter.GovernmentArticleListAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/11/26 16:12
 * description  三务公开-文章列表
 */
class GovernmentArticleListFragment : BaseFragment(), GovernmentArticleListContract.View {

    private lateinit var mId : String
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var isRefresh = false
    private lateinit var mData: List<GovernmentArticleListData.ListBean>
    private lateinit var mAdapter: GovernmentArticleListAdapter
    private val mPresenter by lazy { GovernmentArticleListPresenter() }
    companion object {
        fun getInstance(id: String): GovernmentArticleListFragment {
            val fragment = GovernmentArticleListFragment()
            fragment.mId = id
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
        mAdapter = GovernmentArticleListAdapter(R.layout.layout_life_bulletin_item, mData)
        mAdapter.setOnItemClickListener{ _, _, position ->
            //跳转到详情页面
            val intent = Intent(activity, LifeBulletinDetailActivity::class.java)
            intent.putExtra(Constants.ID,"${mAdapter.data[position].id}")
            intent.putExtra(Constants.TYPE,Constants.ARTICLE)
            startActivity(intent)
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
        mPresenter.getGovernmentArticleListData(uid,page,Constants.SIZE,mId.toInt(),token,null)
    }
    private var isFirst: Boolean = true
    override fun setGovernmentArticleListData(governmentArticleListData: GovernmentArticleListData) {
        mData = governmentArticleListData.list
        when{
            isRefresh -> {
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
                isFirst = false
            }
            else -> {
                if (mData.isNotEmpty()) {
                    mAdapter.addData(mData)
                    mAdapter.notifyDataSetChanged()
                    smart_layout.isEnableLoadMore = mData.size >= Constants.SIZE
                    isFirst = false
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
            ApiErrorCode.NETWORK_ERROR -> if (isFirst) mLayoutStatusView?.showNoNetwork()
            else -> if (isFirst) mLayoutStatusView?.showError()
        }
    }
    override fun showLoading() {
        if (!isRefresh && isFirst) {
            isRefresh = false
            mLayoutStatusView?.showLoading()
        }
    }
    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
}