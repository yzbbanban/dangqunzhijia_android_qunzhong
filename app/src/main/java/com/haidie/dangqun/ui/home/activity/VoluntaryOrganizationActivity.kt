package com.haidie.dangqun.ui.home.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haidie.dangqun.Constants
import com.haidie.dangqun.R
import com.haidie.dangqun.base.BaseActivity
import com.haidie.dangqun.mvp.contract.home.VoluntaryOrganizationContract
import com.haidie.dangqun.mvp.model.bean.VoluntaryOrganizationListData
import com.haidie.dangqun.mvp.presenter.home.VoluntaryOrganizationPresenter
import com.haidie.dangqun.net.exception.ApiErrorCode
import com.haidie.dangqun.ui.home.adapter.VoluntaryOrganizationAdapter
import com.haidie.dangqun.utils.Preference
import com.haidie.dangqun.view.RecyclerViewDividerItemDecoration
import kotlinx.android.synthetic.main.activity_layout_smart_multiple_recycler_view.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/09/11 15:25
 * description  志愿组织
 */
class VoluntaryOrganizationActivity : BaseActivity(),VoluntaryOrganizationContract.View {

    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, Constants.NEGATIVE_ONE)
    private var page: Int = 1
    private var isRefresh = false
    private val mPresenter by lazy { VoluntaryOrganizationPresenter() }
    private lateinit var mData: MutableList<VoluntaryOrganizationListData.VoluntaryOrganizationListItemData>
    private lateinit var mAdapter: VoluntaryOrganizationAdapter
    override fun getLayoutId(): Int = R.layout.activity_layout_smart_multiple_recycler_view
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = "志愿组织"

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
            start()
            it.finishRefresh(1000)
        }
        smart_layout.setOnLoadMoreListener{
            page++
            isRefresh = false
            start()
            it.finishLoadMore(1000)
        }
    }
    private fun initRecyclerView() {
        mData = ArrayList()
        mAdapter = VoluntaryOrganizationAdapter(R.layout.layout_voluntary_organization_item, mData)
        mAdapter.setOnItemClickListener{ _, _, position ->
            val intent = Intent(this@VoluntaryOrganizationActivity, VoluntaryOrganizationDetailActivity::class.java)
            intent.putExtra(Constants.ID,mAdapter.data[position].id)
            intent.putExtra(Constants.TEXT,mAdapter.data[position].title)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
        }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(RecyclerViewDividerItemDecoration(this))
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = mAdapter
        smart_layout.setEnableHeaderTranslationContent(true)
    }
    override fun start() { mPresenter.getVoluntaryOrganizationListData(uid,page,Constants.SIZE,token) }
    private var isFirst: Boolean = true
    override fun setVoluntaryOrganizationListData(voluntaryOrganizationListData: VoluntaryOrganizationListData) {
        mData = voluntaryOrganizationListData.list
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